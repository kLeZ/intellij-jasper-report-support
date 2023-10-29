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
package me.klez.file.type.jrxml;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JrxmlFileTypeFactory {
	@NonNls public static final String JRXML_VERSION = "6.20.6";
	@NonNls public static final String JRXML_EXTENSION = "jrxml";
	@NonNls public static final String VERSION_PLACEHOLDER_TEXT = "JasperReports";
	@NonNls static final String DOT_JRXML_EXTENSION = "." + JRXML_EXTENSION;
	@NonNls private static final Pattern VERSION_PATTERN = Pattern.compile("\\d+\\.\\d+\\.\\d+");

	public static boolean isJrxml(@NotNull PsiFile file) {
		final VirtualFile virtualFile = file.getViewProvider().getVirtualFile();
		return isJrxml(virtualFile);
	}

	public static boolean isJrxml(@NotNull VirtualFile virtualFile) {
		if (JRXML_EXTENSION.equals(virtualFile.getExtension())) {
			final FileType fileType = virtualFile.getFileType();
			if (fileType == getFileType() && !fileType.isBinary()) {
				return virtualFile.getName().endsWith(DOT_JRXML_EXTENSION);
			}
		}
		return false;
	}

	public static String getJrxmlVersion(@NotNull PsiFile file) {
		final VirtualFile virtualFile = file.getViewProvider().getVirtualFile();
		return getJrxmlVersion(virtualFile);
	}

	public static String getJrxmlVersion(@NotNull VirtualFile virtualFile) {
		if (JRXML_EXTENSION.equals(virtualFile.getExtension())) {
			final FileType fileType = virtualFile.getFileType();
			if (fileType == getFileType() && !fileType.isBinary()) {
				return readVersion(virtualFile).orElse(JRXML_VERSION);
			}
		}
		return JRXML_VERSION;
	}

	private static Optional<String> readVersion(@NotNull VirtualFile virtualFile) {
		String version = null;
		try (InputStream inputStream = virtualFile.getInputStream()) {
			LineIterator result = IOUtils.lineIterator(inputStream, StandardCharsets.UTF_8);
			int linesScanned = 0;
			while (result.hasNext() && version == null && linesScanned++ < 10) {
				String line = result.next();
				if (line.contains(VERSION_PLACEHOLDER_TEXT)) {
					String versionLine = StringUtils.substringAfter(line, VERSION_PLACEHOLDER_TEXT);
					Matcher matcher = VERSION_PATTERN.matcher(versionLine);
					if (matcher.find()) {
						version = matcher.group();
					}
				}
			}
		} catch (IOException ignored) {
		}
		return Optional.ofNullable(version);
	}

	@NotNull
	public static FileType getFileType() {
		return FileTypeManager.getInstance().getFileTypeByExtension(JRXML_EXTENSION);
	}
}
