/**
 * ProjectImpl.java
 * Created on 11.02.2003, 23:06:22 Alex
 * Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */

package main.java.memoranda;

import main.java.memoranda.date.*;
import nu.xom.*;

/**
 * Default implementation of Project interface
 */
/*$Id: ProjectImpl.java,v 1.7 2004/11/22 10:02:37 alexeya Exp $*/
public class ProjectImpl implements Project {

    private Element element = null;

    /**
     * Constructor for ProjectImpl.
     */
    public ProjectImpl(Element root) {
        element = root;
    }

    /**
     * @see main.java.memoranda.Project#getId()
     */
    public String getId() {
        return element.getAttribute("id").getValue();
    }

    /**
     * @see main.java.memoranda.Project#getStartDate()
     */
    public CalendarDate getStartDate() {
        Attribute d = element.getAttribute("startDate");
        if (d == null) {
            return null;
        }
        return new CalendarDate(d.getValue());
    }

    /**
     * @see main.java.memoranda.Project#setStartDate(net.sf.memoranda.util.CalendarDate)
     */
    public void setStartDate(CalendarDate date) {
        if (date != null) {
            setAttr("startDate", date.toString());
        }
    }

    /**
     * @see main.java.memoranda.Project#getEndDate()
     */
    public CalendarDate getEndDate() {
        Attribute d = element.getAttribute("endDate");
        if (d == null) {
            return null;
        }
        return new CalendarDate(d.getValue());
    }

    /**
     * @see main.java.memoranda.Project#setEndDate(net.sf.memoranda.util.CalendarDate)
     */
    public void setEndDate(CalendarDate date) {
        if (date != null) {
            setAttr("endDate", date.toString());
        }
        else if (element.getAttribute("endDate") != null) {
            setAttr("endDate", null);
        }
    }

    /**
     * @see main.java.memoranda.Project#getStatus()
     */
    public int getStatus() {
        if (isFrozen()) {
            return Project.FROZEN;
        }
        CalendarDate today = CurrentDate.get();
        CalendarDate prStart = getStartDate();
        CalendarDate prEnd = getEndDate();
        if (prEnd == null) {
            if (today.before(prStart)) {
                return Project.SCHEDULED;
            } else {
                return Project.ACTIVE;
            }
        }
        if (today.inPeriod(prStart, prEnd)) {
            return Project.ACTIVE;
        }
        else if (today.after(prEnd)) {
            //if (getProgress() == 100)
            return Project.COMPLETED;
            /*else
                return Project.FAILED;*/
        } else {
            return Project.SCHEDULED;
        }
    }

    private boolean isFrozen() {
        return element.getAttribute("frozen") != null;
    }

   
    /*public int getProgress() {
        Vector v = getAllTasks();
        if (v.size() == 0) return 0;
        int p = 0;
        for (Enumeration en = v.elements(); en.hasMoreElements();) {
          Task t = (Task) en.nextElement();
          p += t.getProgress();
        }
        return (p*100)/(v.size()*100);
    }*/


    /**
     * @see main.java.memoranda.Project#freeze()
     */
    public void freeze() {
        element.addAttribute(new Attribute("frozen", "yes"));
    }

    /**
     * @see main.java.memoranda.Project#unfreeze()
     */
    public void unfreeze() {
        if (this.isFrozen()) {
            element.removeAttribute(new Attribute("frozen", "yes"));
        }
    }

    /**
     * @see main.java.memoranda.Project#getTitle()
     */
    public String getTitle() {
        Attribute ta = element.getAttribute("title");
        if (ta != null) {
            return ta.getValue();
        }
        return "";
    }

    /**
     * @see main.java.memoranda.Project#setTitle(java.lang.String)
     */
    public void setTitle(String title) {
        setAttr("title", title);
    }

    private void setAttr(String name, String value) {
        Attribute a = element.getAttribute(name);
        if (a == null) {
            if (value != null) {
                element.addAttribute(new Attribute(name, value));
            }
        } else if (value != null) {
            a.setValue(value);
        }
        else {
            element.removeAttribute(a);
        }
    }

    public String getDescription() {
        Element thisElement = element.getFirstChildElement("description");
        if (thisElement == null) {
            return null;
        } else {
            return thisElement.getValue();
        }
    }

    public void setDescription(String s) {
        Element desc = element.getFirstChildElement("description");
        if (desc == null) {
            desc = new Element("description");
            desc.appendChild(s);
            element.appendChild(desc);
        } else {
            desc.removeChildren();
            desc.appendChild(s);
        }
    }

    /**
     * @see net.sf.memoranda.Project#getTaskList()
     */
    /*public TaskList getTaskList() {
        return CurrentStorage.get().openTaskList(this);
    }*/
    /**
     * @see net.sf.memoranda.Project#getNoteList()
     */
    /*public NoteList getNoteList() {
        return CurrentStorage.get().openNoteList(this);
    }*/
    /**
     * @see net.sf.memoranda.Project#getResourcesList()
     */
    /*public ResourcesList getResourcesList() {
        return CurrentStorage.get().openResourcesList(this);
    }*/
}
