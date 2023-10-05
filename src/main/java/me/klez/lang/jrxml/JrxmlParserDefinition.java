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
package me.klez.lang.jrxml;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.lexer.XmlLexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.parsing.xml.XmlParser;
import com.intellij.psi.impl.source.xml.XmlFileImpl;
import com.intellij.psi.impl.source.xml.stub.XmlStubBasedElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.xml.XmlElementType;
import com.intellij.psi.xml.XmlTokenType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public class JrxmlParserDefinition implements ParserDefinition {
	public static final IFileElementType FILE = new IFileElementType(JrxmlLanguage.INSTANCE);

	public static SpaceRequirements canStickTokensTogether(final ASTNode left, final ASTNode right) {
		if (left.getElementType() == XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN || right.getElementType() == XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN) {
			return SpaceRequirements.MUST_NOT;
		}
		if (left.getElementType() == XmlTokenType.XML_ATTRIBUTE_VALUE_END_DELIMITER && right.getElementType() == XmlTokenType.XML_NAME) {
			return SpaceRequirements.MUST;
		}
		if (left.getElementType() == XmlTokenType.XML_NAME && right.getElementType() == XmlTokenType.XML_NAME) {
			return SpaceRequirements.MUST;
		}
		if (left.getElementType() == XmlTokenType.XML_TAG_NAME && right.getElementType() == XmlTokenType.XML_NAME) {
			return SpaceRequirements.MUST;
		}
		return SpaceRequirements.MAY;
	}

	@Override
	@NotNull
	public Lexer createLexer(Project project) {
		return new XmlLexer();
	}

	@Override
	public @NotNull IFileElementType getFileNodeType() {
		return XmlElementType.XML_FILE;
	}

	@Override
	@NotNull
	public TokenSet getWhitespaceTokens() {
		return XmlTokenType.WHITESPACES;
	}

	@Override
	@NotNull
	public TokenSet getCommentTokens() {
		return XmlTokenType.COMMENTS;
	}

	@Override
	@NotNull
	public TokenSet getStringLiteralElements() {
		return TokenSet.EMPTY;
	}

	@Override
	@NotNull
	public PsiParser createParser(final Project project) {
		return new XmlParser();
	}

	@Override
	@NotNull
	public PsiElement createElement(ASTNode node) {
		if (node.getElementType() instanceof XmlStubBasedElementType<?, ?> xmlStubBasedElementType) {
			return xmlStubBasedElementType.createPsi(node);
		}
		throw new IncorrectOperationException("I don't know how to create XML PSI for this node: %s (%s)".formatted(node, node.getClass()));
	}

	@Override
	public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
		return new XmlFileImpl(viewProvider, XmlElementType.XML_FILE);
	}

	@Override
	public @NotNull SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
		return canStickTokensTogether(left, right);
	}
}
