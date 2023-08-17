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

package me.klez.actions;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompileJrxmlAction extends AnAction {
	public static final String SOURCE_EXT = ".jrxml";
	public static final String DEST_EXT = ".jasper";
	public static final String NOTIFICATION_GROUP = "Jasper Report Support Notification Group";

	private static Result buildReport(VirtualFile vf) {
		final Result.ResultBuilder builder = Result.builder().source(vf.getPath()).destination(vf.getPath().replace(SOURCE_EXT, DEST_EXT));
		try {
			JasperCompileManager.compileReportToFile(builder.getSource(), builder.getDestination());
			builder.type(ResultType.OK);
		} catch (JRException e) {
			builder.type(ResultType.KO).message(e.getMessage()).exception(e);
		}
		return builder.build();
	}

	private static String getResultMessage(Result r) {
		return String.format("%s: %s%s", r.getSource(), r.getType(), getKoMessage(r));
	}

	private static String getKoMessage(Result r) {
		return r.getType() == ResultType.KO ? String.format(" (%s)", r.getMessage()) : "";
	}

	/**
	 * Implement this method to provide your action handler.
	 *
	 * @param e
	 * 		Carries information on the invocation place
	 */
	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		if (ActionPlaces.isMainMenuOrActionSearch(e.getPlace())) {
			Optional.ofNullable(e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY))
			        .map(Stream::of)
			        .map(stream -> stream.filter(vf -> vf.isValid() && vf.exists() && !vf.isDirectory())
			                             .filter(vf -> vf.getPath().endsWith(SOURCE_EXT))
			                             .map(CompileJrxmlAction::buildReport)
			                             .collect(Collectors.toList()))
			        .ifPresent(resultList -> createBalloon(resultList, e.getProject()));
		}
	}

	private void createBalloon(Collection<Result> resultList, Project project) {
		long okCount = resultList.stream().filter(r -> r.getType() == ResultType.OK).count();
		long koCount = resultList.stream().filter(r -> r.getType() == ResultType.KO).count();
		NotificationType notificationType;
		if (koCount == 0) {
			notificationType = NotificationType.INFORMATION;
		} else {
			if (okCount == 0) {
				notificationType = NotificationType.ERROR;
			} else {
				notificationType = NotificationType.WARNING;
			}
		}
		String content = resultList.stream().map(CompileJrxmlAction::getResultMessage).collect(Collectors.joining(System.lineSeparator()));
		NotificationGroupManager.getInstance().getNotificationGroup(NOTIFICATION_GROUP).createNotification(content, notificationType).notify(project);
	}
}
