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
package me.klez.util;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public class JrxmlIcons {
	public static final Icon JRXML = IconLoader.getIcon("/me/klez/icons/jrxml.png", JrxmlIcons.class.getClassLoader());
	public static final Icon JASPER = IconLoader.getIcon("/me/klez/icons/jasper.png", JrxmlIcons.class.getClassLoader());

	private JrxmlIcons() {
		throw new IllegalStateException("Cannot initialize JrxmlIcons");
	}
}
