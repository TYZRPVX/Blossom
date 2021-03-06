package blossom.ready.lint;

import com.android.tools.lint.client.api.JavaParser;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import java.util.Collections;
import java.util.List;

import lombok.ast.AstVisitor;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.MethodInvocation;
import lombok.ast.Node;

/**
 * 避免使用Log / System.out.println ,提醒使用Ln
 * <p>
 * RoboGuice's Ln logger is similar to Log, but has the following advantages:
 * - Debug and verbose logging are automatically disabled for release builds.
 * - Your app name, file and line of the log message, time stamp, thread, and other useful information is automatically logged for you. (Some of this information is disabled for release builds to improve performance).
 * - Performance of disabled logging is faster than Log due to the use of the varargs. Since your most expensive logging will often be debug or verbose logging, this can lead to a minor performance win.
 * - You can override where the logs are written to and the format of the logging.
 * <p>
 * https://github.com/roboguice/roboguice/wiki/Logging-via-Ln
 * <p>
 * Created by chentong on 18/9/15.
 */
public class LogDetector extends Detector implements Detector.JavaScanner {

    public static final Issue ISSUE = Issue.create(
            "LogUse",
            "避免使用Log/System.out.println",
            "使用Ln，防止在正式包打印log",
            Category.SECURITY, 5, Severity.ERROR,
            new Implementation(LogDetector.class, Scope.JAVA_FILE_SCOPE));

    @Override
    public List<Class<? extends Node>> getApplicableNodeTypes() {
        return Collections.<Class<? extends Node>>singletonList(MethodInvocation.class);
    }

    @Override
    public AstVisitor createJavaVisitor(final JavaContext context) {
        return new ForwardingAstVisitor() {
            @Override
            public boolean visitMethodInvocation(MethodInvocation node) {

                if (node.toString().startsWith("System.out.println")) {
                    context.report(ISSUE, node, context.getLocation(node),
                            "请使用Ln，避免使用System.out.println");
                    return true;
                }

                JavaParser.ResolvedNode resolve = context.resolve(node);
                if (resolve instanceof JavaParser.ResolvedMethod) {
                    JavaParser.ResolvedMethod method = (JavaParser.ResolvedMethod) resolve;
                    // 方法所在的类校验
                    JavaParser.ResolvedClass containingClass = method.getContainingClass();
                    if (containingClass.matches("android.util.Log")) {
                        context.report(ISSUE, node, context.getLocation(node),
                                "请使用Ln，避免使用Log");
                        return true;
                    }
                }
                return super.visitMethodInvocation(node);
            }
        };
    }
}
