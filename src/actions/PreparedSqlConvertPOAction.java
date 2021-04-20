package actions;

import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang.StringUtils;
import util.SqlUtil;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class PreparedSqlConvertPOAction extends AnAction {
    private static final NotificationGroup NOTIFICATION_GROUP = NotificationGroupManager.getInstance().getNotificationGroup("SqlHelper.NotificationGroup");

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Editor editor = (Editor)e.getData(PlatformDataKeys.EDITOR);
        if (editor != null) {
            Project project = editor.getProject();
            if (project != null) {
                SelectionModel model = editor.getSelectionModel();
                String selectedMybatisLogs = model.getSelectedText();
                if (!StringUtils.isBlank(selectedMybatisLogs)) {
                    String sql;
                    try {
                        sql = SqlUtil.parse(selectedMybatisLogs);
                    } catch (Exception ex) {
                        this.notify(project, String.format("Failed at: %s", ex.toString()), NotificationType.ERROR);
                        return;
                    }
                    if (StringUtils.isBlank(sql)) {
                        this.notify(project, String.format("Selected area should contain both [%s] in the 1st line and [%s] in the 2nd line.", "Preparing:", "Parameters:"), NotificationType.WARNING);
                    } else {
                        StringSelection selection = new StringSelection(sql);
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(selection, selection);
                        this.notify(project, "Success, copied to clipboard.", NotificationType.INFORMATION);
                    }
                }
            }
        }

    }


    private void notify(Project project, String message, NotificationType type) {
        Notification success = NOTIFICATION_GROUP.createNotification(message, type);
        Notifications.Bus.notify(success, project);
    }
}
