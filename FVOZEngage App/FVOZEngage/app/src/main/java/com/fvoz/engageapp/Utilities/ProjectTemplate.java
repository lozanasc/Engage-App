package com.fvoz.engageapp.Utilities;

public class ProjectTemplate {

    private String ProjectTitle;
    private String ProjectDescription;
    private String ProjectImage;
    private boolean ProjectStatus;

    public ProjectTemplate(){}
    ProjectTemplate(String Title, String Description, String Image, boolean Status){
        this.ProjectTitle = Title;
        this.ProjectDescription = Description;
        this.ProjectImage = Image;
        this.ProjectStatus = Status;
    }


    public String getProjectTitle() {
        return ProjectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        ProjectTitle = projectTitle;
    }

    public String getProjectDescription() {
        return ProjectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        ProjectDescription = projectDescription;
    }

    public boolean isProjectStatus() {
        return ProjectStatus;
    }

    public void setProjectStatus(boolean projectStatus) {
        ProjectStatus = projectStatus;
    }

    public String getProjectImage() {
        return ProjectImage;
    }

    public void setProjectImage(String projectImage) {
        ProjectImage = projectImage;
    }
}
