package com.surenpi.jenkins.phoenix.steps;

import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.cloudbees.plugins.credentials.common.StandardUsernameCredentials;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.surenpi.jenkins.phoenix.DurableExecution;
import com.surenpi.jenkins.phoenix.DurableStep;
import com.surenpi.jenkins.phoenix.DurableTaskStepDescriptor;
import hudson.Extension;
import hudson.model.FreeStyleProject;
import hudson.security.ACL;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.durabletask.DurableTask;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author suren
 */
public class FtpStep extends DurableStep implements Serializable
{
    private String serverHost;
    private int serverPort = 21;
    private String credentialsId;

    private String srcFile;
    private String targetDir;

    @DataBoundConstructor
    public FtpStep(String serverHost, String srcFile, String targetDir)
    {
        this.serverHost = serverHost;
        this.srcFile = srcFile;
        this.targetDir = targetDir;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception
    {
        return new DurableExecution(context, this);
    }

    @Override
    public DurableTask task()
    {
        return new DurableFtpTask(this);
    }

    @Extension
    public static final class DescriptorImpl extends DurableTaskStepDescriptor
    {
        public ListBoxModel doFillCredentialsIdItems() {
            FreeStyleProject project = new FreeStyleProject(Jenkins.getInstance(), "fake-" + UUID.randomUUID().toString());

            return new StandardListBoxModel().includeEmptyValue()
                    .includeMatchingAs(ACL.SYSTEM, project,
                            StandardUsernameCredentials.class,
                            new ArrayList<DomainRequirement>(),
                            CredentialsMatchers.withScopes(CredentialsScope.GLOBAL));
        }

        @Override
        public String getFunctionName()
        {
            return "ftp";
        }

        @Override
        public String getDisplayName()
        {
            return "FtpStep";
        }
    }

    public String getServerHost()
    {
        return serverHost;
    }

    @DataBoundSetter
    public void setServerHost(String serverHost)
    {
        this.serverHost = serverHost;
    }

    public int getServerPort()
    {
        return serverPort;
    }

    @DataBoundSetter
    public void setServerPort(int serverPort)
    {
        this.serverPort = serverPort;
    }

    public String getCredentialsId()
    {
        return credentialsId;
    }

    @DataBoundSetter
    public void setCredentialsId(String credentialsId)
    {
        this.credentialsId = credentialsId;
    }

    public String getSrcFile()
    {
        return srcFile;
    }

    @DataBoundSetter
    public void setSrcFile(String srcFile)
    {
        this.srcFile = srcFile;
    }

    public String getTargetDir()
    {
        return targetDir;
    }

    @DataBoundSetter
    public void setTargetDir(String targetDir)
    {
        this.targetDir = targetDir;
    }
}