/*
 * Project intellij-jasper-report-support
 *
 * Copyright 2023-2023 Alessandro 'kLeZ' Accardo
 * Previous copyright (c) 2017-2023 Chathura Buddhika
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package me.klez.lang.jrxml.autocomplete;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.xml.XmlSchemaProvider;
import me.klez.file.type.jrxml.JrxmlFileTypeFactory;
import net.sf.jasperreports.components.ComponentsExtensionsRegistryFactory;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.xml.JRXmlConstants;
import net.sf.jasperreports.engine.xml.JRXmlDigester;
import net.sf.jasperreports.engine.xml.JRXmlDigesterFactory;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

public class JrxmlSchemaProvider extends XmlSchemaProvider {
	private static final Logger LOG = Logger.getInstance(JrxmlSchemaProvider.class);

	@Nullable
	private static URL getSchemaResource(@NotNull String url, String version) {
		LOG.debug("Using version %s for schema %s".formatted(version, url));
		String schemaResourceName = getSchemaResourceName(url, version);
		String defaultSchemaResourceName = getSchemaResourceName(url, JrxmlFileTypeFactory.JRXML_VERSION);
		LOG.debug("Using schema %s, if can't find it I'll use default schema %s".formatted(schemaResourceName, defaultSchemaResourceName));
		return Optional.ofNullable(JrxmlSchemaProvider.class.getResource(schemaResourceName))
		               .orElseGet(() -> JrxmlSchemaProvider.class.getResource(defaultSchemaResourceName));
	}

	private static String getSchemaResourceName(@NotNull String url, @NotNull String version) {
		// http://jasperreports.sourceforge.net/jasperreports/components http://jasperreports.sourceforge.net/xsd/components.xsd
		String xsdUrlPath = StringUtils.removeStart(URI.create(url).getPath(), "/");
		String xsdPath;
		if (xsdUrlPath.endsWith(".xsd")) {
			xsdPath = xsdUrlPath;
		} else {
			xsdPath = "xsd/%s.xsd".formatted(xsdUrlPath.substring(xsdUrlPath.lastIndexOf("/") + 1));
		}
		LOG.debug("Trying to build a schema path with this xsdPath: " + xsdPath);
		String versionPath = version.replaceAll("\\.", "_");
		return "/me/klez/schemas/%s/%s".formatted(versionPath, xsdPath);
	}

	@Nullable
	private static URL getLibSchemaResource(@NotNull String url, String version) {
		LOG.debug("Using version %s for schema %s".formatted(version, url));
		String schemaResourceName = getLibSchemaResourceName(url, version);
		String defaultSchemaResourceName = getSchemaResourceName(url, JrxmlFileTypeFactory.JRXML_VERSION);
		LOG.debug("Using schema %s, if can't find it I'll use default schema %s".formatted(schemaResourceName, defaultSchemaResourceName));
		return Optional.ofNullable(JRConstants.class.getResource(schemaResourceName)).orElseGet(() -> JRConstants.class.getResource(defaultSchemaResourceName));
	}

	private static String getLibSchemaResourceName(@NotNull String url, @NotNull String version) {
		if (isJasperReportSchema(url)) {
			return JRXmlConstants.JASPERREPORT_XSD_RESOURCE;
		} else if (isJasperPrintSchema(url)) {
			return JRXmlConstants.JASPERPRINT_XSD_RESOURCE;
		} else if (isJasperTemplateSchema(url)) {
			return JRXmlConstants.JASPERTEMPLATE_XSD_RESOURCE;
		} else if (isComponentExtensionsSchema(url)) {
			return ComponentsExtensionsRegistryFactory.XSD_RESOURCE;
		}
		return "noreport.xsd";
	}

	private static boolean isJasperSchema(@NotNull String url) {
		return isJasperReportSchema(url) || isJasperPrintSchema(url) || isJasperTemplateSchema(url) || isComponentExtensionsSchema(url);
	}

	private static boolean isJasperReportSchema(@NotNull String url) {
		// @formatter:off
		return StringUtils.equalsAnyIgnoreCase(url,
				JRXmlConstants.JASPERREPORTS_NAMESPACE, JRXmlConstants.JASPERREPORT_XSD_SYSTEM_ID
		);
		// @formatter:on
	}

	private static boolean isJasperPrintSchema(@NotNull String url) {
		// @formatter:off
		return StringUtils.equalsAnyIgnoreCase(url,
				JRXmlConstants.JASPERPRINT_NAMESPACE, JRXmlConstants.JASPERPRINT_XSD_SYSTEM_ID
		);
		// @formatter:on
	}

	private static boolean isJasperTemplateSchema(@NotNull String url) {
		// @formatter:off
		return StringUtils.equalsAnyIgnoreCase(url,
				JRXmlConstants.JASPERTEMPLATE_NAMESPACE, JRXmlConstants.JASPERTEMPLATE_XSD_SYSTEM_ID
		);
		// @formatter:on
	}

	private static boolean isComponentExtensionsSchema(@NotNull String url) {
		// @formatter:off
		return StringUtils.equalsAnyIgnoreCase(url,
				ComponentsExtensionsRegistryFactory.NAMESPACE, ComponentsExtensionsRegistryFactory.XSD_LOCATION
		);
		// @formatter:on
	}

	private static Optional<VirtualFile> findVirtualFileForSchema(@NotNull String url, @NotNull PsiFile baseFile) {
		String version = JrxmlFileTypeFactory.getJrxmlVersion(baseFile);
		final URL resource = getSchemaResource(url, version);
		if (resource != null) {
			return Optional.ofNullable(VfsUtil.findFileByURL(resource));
		}
		return Optional.empty();
	}

	private static boolean verify(VirtualFile virtualFile, String fileName) throws ParserConfigurationException, SAXException {
		try (InputStream inputStream = virtualFile.getInputStream()) {
			JasperReportsContext context = DefaultJasperReportsContext.getInstance();
			JRXmlDigester digester = JRXmlDigesterFactory.createDigester(context);
			digester.setValidating(true);
			digester.parse(inputStream);
			return true;
		} catch (IOException e) {
			LOG.error("Error parsing file %s. This is not a valid Jasper report file.\n%s".formatted(fileName, e.getMessage()), e);
			return false;
		}
	}

	@Override
	public boolean isAvailable(final @NotNull XmlFile file) {
		return JrxmlFileTypeFactory.isJrxml(file);
	}

	@Nullable
	@Override
	public XmlFile getSchema(@NotNull String url, @Nullable Module module, @NotNull PsiFile baseFile) {
		LOG.debug("getSchema(%s)".formatted(url));
		if (module != null && JrxmlFileTypeFactory.isJrxml(baseFile) && isJasperSchema(url)) {
			Optional<VirtualFile> virtualFile = Optional.ofNullable(baseFile.getVirtualFile());
			if (virtualFile.isPresent()) {
				String fileName = virtualFile.map(VirtualFile::getName).get();
				LOG.debug("Requested schema %s for the report %s".formatted(url, fileName));
				Optional<VirtualFile> optSchemaFile = findVirtualFileForSchema(url, baseFile);
				if (optSchemaFile.isPresent()) {
					VirtualFile file = optSchemaFile.get();
					LOG.debug("Successfully loaded schema %s".formatted(file.getPath()));
					Optional<PsiFile> psiFile = Optional.ofNullable(PsiManager.getInstance(module.getProject()).findFile(file));
					if (psiFile.isPresent()) {
						return (XmlFile) psiFile.get().copy();
					} else {
						LOG.error("xsd %s not found".formatted(url));
					}
				} else {
					LOG.error("xsd %s not found".formatted(url));
				}
			}
		}
		return null;
	}
}
