package ru.easyjava.maven;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Goal which changes a greeted name.
 */
@Mojo(name = "setname", defaultPhase = LifecyclePhase.PROCESS_CLASSES, threadSafe = true)
public class NamingMojo extends AbstractMojo {
    @Parameter(property = "name")
    private String name;

    @Parameter( defaultValue = "${project}", readonly = true )
    private MavenProject project;

    public void execute() throws MojoExecutionException {
        getLog().info("Updating greeter target");

        ClassPool pool = ClassPool.getDefault();
        try {
            for( String cp : project.getCompileClasspathElements()) {
                pool.insertClassPath(cp);
            }
            CtClass ct = pool.get("ru.easyjava.maven.Target");
            CtField field = ct.getField("NAME");
            getLog().info(String.format("%s %s", name, field.getConstantValue()));
        } catch (NotFoundException | DependencyResolutionRequiredException e) {
            getLog().error(e);
            throw new MojoExecutionException(e.getMessage());
        }
    }
}
