package com.fvoz.engageapp.Utilities;

public class EventsTemplate {

    private String EventTitle;
    private String EventDate;
    private String EventDescription;

    public EventsTemplate(){}
    EventsTemplate(String Title, String Date, String Description){
        this.EventTitle = Title;
        this.EventDate = Date;
        this.EventDescription = Description;
    }

    public String getEventDescription() {
        return EventDescription;
    }

    public void setEventDescription(String eventDescription) {
        EventDescription = eventDescription;
    }

    public String getEventDate() {
        return EventDate;
    }

    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }

    public String getEventTitle() {
        return EventTitle;
    }

    public void setEventTitle(String eventTitle) {
        EventTitle = eventTitle;
    }
}
