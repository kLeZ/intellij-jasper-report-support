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

import com.intellij.openapi.fileTypes.LanguageFileType;
import me.klez.lang.jrxml.JrxmlLanguage;
import me.klez.util.JrxmlIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class JrxmlFileType extends LanguageFileType {
	public static final JrxmlFileType INSTANCE = new JrxmlFileType();

	private JrxmlFileType() {
		super(JrxmlLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getName() {
		return "Jasper source file";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "Jasper report source file, written in XML";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return "jrxml";
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return JrxmlIcons.JRXML;
	}
}
