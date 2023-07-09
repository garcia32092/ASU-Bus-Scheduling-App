/**
 * HistoryItem.java
 * Created on 07.03.2003, 18:31:39 Alex
 * Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */

package main.java.memoranda;

import main.java.memoranda.date.*;

/**
 *
 */
/*$Id: HistoryItem.java,v 1.4 2004/10/06 19:15:43 ivanrise Exp $*/
public class HistoryItem {

    private final CalendarDate calendarDate;
    private final Project project;

    /**
     * Constructor for HistoryItem.
     */
    public HistoryItem(CalendarDate date, Project project) {
        calendarDate = date;
        this.project = project;
    }

    public HistoryItem(Note note) {
        calendarDate = note.getDate();
        project = note.getProject();
    }

    public CalendarDate getDate() {
        return calendarDate;
    }

    public Project getProject() {
        return project;
    }

    public boolean equals(HistoryItem i) {
        return i.getDate().equals(calendarDate) && i.getProject().getId().equals(project.getId());
    }

}
