/**
 * NoteListImpl.java
 * Created on 21.02.2003, 15:43:26 Alex
 * Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */

package main.java.memoranda;

import main.java.memoranda.date.*;
import main.java.memoranda.util.*;
import nu.xom.*;

import java.util.*;

/**
 *
 */
/*$Id: NoteListImpl.java,v 1.14 2004/10/28 11:30:15 alexeya Exp $*/
public class NoteListImpl implements NoteList {

    private Project project = null;
    private Document doc = null;
    private Element root = null;

//    public static final String NS_JNNL = "http://www.openmechanics.org/2003/jnotes-noteslist";

    /**
     * Constructor for NoteListImpl.
     */
    public NoteListImpl(Document doc, Project prj) {
        this.doc = doc;
        root = this.doc.getRootElement();
        project = prj;
    }

    public NoteListImpl(Project prj) {

        //root = new Element("noteslist", NS_JNNL);
        root = new Element("noteslist");
        doc = new Document(root);
        project = prj;
    }

    public Collection getAllNotes() {
        Vector v = new Vector();
        Elements yrs = root.getChildElements("year");
        for (int yi = 0; yi < yrs.size(); yi++) {
            Year y = new Year(yrs.get(yi));
            Vector ms = y.getMonths();
            for (int mi = 0; mi < ms.size(); mi++) {
                Month m = (Month) ms.get(mi);
                Vector ds = m.getDays();
                for (int di = 0; di < ds.size(); di++) {
                    Day d = (Day) ds.get(di);
                    Vector ns = d.getNotes();
                    for (int ni = 0; ni < ns.size(); ni++) {
                        NoteElement n = (NoteElement) ns.get(ni);
                        v.add(new NoteImpl(n.getElement(), project));
                    }
                }
            }
        }
        return v;
    }

    /**
     * @see main.java.memoranda.NoteList#getMarkedNotes()
     */
    public Collection getMarkedNotes() {
        Vector v = new Vector();
        Elements yrs = root.getChildElements("year");
        for (int yi = 0; yi < yrs.size(); yi++) {
            Year y = new Year(yrs.get(yi));
            Vector ms = y.getMonths();
            for (int mi = 0; mi < ms.size(); mi++) {
                Month m = (Month) ms.get(mi);
                Vector ds = m.getDays();
                for (int di = 0; di < ds.size(); di++) {
                    Day d = (Day) ds.get(di);
                    Vector ns = d.getNotes();
                    for (int ni = 0; ni < ns.size(); ni++) {
                        NoteElement ne = (NoteElement) ns.get(ni);
                        Note n = new NoteImpl(ne.getElement(), project);
                        if (n.isMarked()) {
                            v.add(n);
                        }
                    }
                }
            }
        }
        return v;
    }

    public Collection getNotesForPeriod(CalendarDate startDate, CalendarDate endDate) {
        Vector v = new Vector();
        Elements yrs = root.getChildElements("year");
        for (int yi = 0; yi < yrs.size(); yi++) {
            Year y = new Year(yrs.get(yi));
            if ((y.getValue() >= startDate.getYear()) && (y.getValue() <= endDate.getYear())) {
                Vector months = y.getMonths();
                for (int mi = 0; mi < months.size(); mi++) {
                    Month m = (Month) months.get(mi);
                    if (!((y.getValue() == startDate.getYear()) && (m.getValue() < startDate.getMonth()))
                        || !((y.getValue() == endDate.getYear()) && (m.getValue() > endDate.getMonth()))) {
                        Vector days = m.getDays();
                        for (int di = 0; di < days.size(); di++) {
                            Day d = (Day) days.get(di);
                            if (!((m.getValue() == startDate.getMonth()) && (d.getValue() < startDate.getDay()))
                                || !((m.getValue() == endDate.getMonth()) && (d.getValue() > endDate.getDay()))) {
                                Vector ns = d.getNotes();
                                for (int ni = 0; ni < ns.size(); ni++) {
                                    NoteElement n = (NoteElement) ns.get(ni);
                                    v.add(new NoteImpl(n.getElement(), project));
                                }
                            }
                        }
                    }
                }
            }
        }
        return v;
    }

    /**
     * returns the first note for a date.
     *
     * @param date
     * @return Note
     */

    public Note getNoteForDate(CalendarDate date) {
        Day d = getDay(date);
        if (d == null) {
            return null;
        }
        Vector ns = d.getNotes();
        if (ns.size() > 0) {
            NoteElement n = (NoteElement) ns.get(0);
            Note currentNote = new NoteImpl(n.getElement(), project);
            return currentNote;
        }
        return null;
        //return new NoteImpl(d.getElement(), _project);
    }

    public Note createNoteForDate(CalendarDate date) {
        Year y = getYear(date.getYear());
        if (y == null) {
            y = createYear(date.getYear());
        }
        Month m = y.getMonth(date.getMonth());
        if (m == null) {
            m = y.createMonth(date.getMonth());
        }
        Day d = m.getDay(date.getDay());
        if (d == null) {
            d = m.createDay(date.getDay());
        }
        NoteElement ne = d.createNote(Util.generateId());
        return new NoteImpl(ne.getElement(), project);
    }

    public void removeNote(CalendarDate date, String id) {
        Day d = getDay(date);
        if (d == null) {
            return;
        }
        Vector ns = d.getNotes();
        for (int i = 0; i < ns.size(); i++) {
            NoteElement n = (NoteElement) ns.get(i);
            Element ne = n.getElement();
            if (ne.getAttribute("refid").getValue().equals(id)) {
                d.getElement().removeChild(n.getElement());
            }
        }
    }

    public Note getActiveNote() {
        //return CurrentNote.get(); 
        return getNoteForDate(CurrentDate.get());
        // FIXED: Must return the first note for today [alexeya]
    }

    private Year getYear(int y) {
        Elements yrs = root.getChildElements("year");
        String yy = Integer.valueOf(y).toString();
        for (int i = 0; i < yrs.size(); i++) {
            if (yrs.get(i).getAttribute("year").getValue().equals(yy)) {
                return new Year(yrs.get(i));
            }
        }
        //return createYear(y);
        return null;
    }

    private Year createYear(int y) {
        Element el = new Element("year");
        el.addAttribute(new Attribute("year", Integer.valueOf(y).toString()));
        root.appendChild(el);
        return new Year(el);
    }

    /*
        private Vector getYears() {
            Vector v = new Vector();
            Elements yrs = root.getChildElements("year");
            for (int i = 0; i < yrs.size(); i++)
                v.add(new Year(yrs.get(i)));
            return v;
        }
    */
    private Day getDay(CalendarDate date) {
        Year y = getYear(date.getYear());
        if (y == null) {
            return null;
        }
        Month m = y.getMonth(date.getMonth());
        if (m == null) {
            return null;
        }
        return m.getDay(date.getDay());
    }

    private class Year {
        Element yearElement = null;

        public Year(Element el) {
            yearElement = el;
        }

        public int getValue() {
            return Integer.parseInt(yearElement.getAttribute("year").getValue());
        }

        public Month getMonth(int m) {
            Elements ms = yearElement.getChildElements("month");
            String mm = Integer.valueOf(m).toString();
            for (int i = 0; i < ms.size(); i++) {
                if (ms.get(i).getAttribute("month").getValue().equals(mm)) {
                    return new Month(ms.get(i));
                }
            }

            return null;
        }

        private Month createMonth(int m) {
            Element el = new Element("month");
            el.addAttribute(new Attribute("month", Integer.valueOf(m).toString()));
            yearElement.appendChild(el);
            return new Month(el);
        }

        public Vector getMonths() {
            Vector v = new Vector();
            Elements ms = yearElement.getChildElements("month");
            for (int i = 0; i < ms.size(); i++) {
                v.add(new Month(ms.get(i)));
            }
            return v;
        }

        public Element getElement() {
            return yearElement;
        }

    }

    private class Month {
        Element element;

        public Month(Element el) {
            element = el;
        }

        public int getValue() {
            return Integer.parseInt(element.getAttribute("month").getValue());
        }

        public Day getDay(int d) {
            if (element == null) {
                return null;
            }
            Elements ds = element.getChildElements("day");
            String dd = Integer.valueOf(d).toString();
            for (int i = 0; i < ds.size(); i++) {
                if (ds.get(i).getAttribute("day").getValue().equals(dd)) {
                    return new Day(ds.get(i));
                }
            }
            //return createDay(d);
            return null;
        }

        private Day createDay(int d) {
            Element el = new Element("day");
            el.addAttribute(new Attribute("day", Integer.valueOf(d).toString()));
            element.appendChild(el);
            return new Day(el);
        }

        public Vector getDays() {
            if (element == null) {
                return null;
            }
            Vector v = new Vector();
            Elements ds = element.getChildElements("day");
            for (int i = 0; i < ds.size(); i++) {
                v.add(new Day(ds.get(i)));
            }
            return v;
        }

        public Element getElement() {
            return element;
        }

    }

    private class Day {
        Element element;

        public Day(Element el) {
            element = el;
            // Added to fix old '.notes' XML format 
            // Old-style XML is converted on the fly [alexeya]
            if (element.getAttribute("date") != null) {
                Attribute dAttr = element.getAttribute("date");
                Attribute tAttr = element.getAttribute("title");
                Element nEl = new Element("note");
                String date = dAttr.getValue().replace('/', '-');
                nEl.addAttribute(new Attribute("refid", date));
                nEl.addAttribute(new Attribute("title", tAttr.getValue()));
                element.appendChild(nEl);
                element.removeAttribute(dAttr);
                element.removeAttribute(tAttr);
            }
        }

        public int getValue() {
            return Integer.parseInt(element.getAttribute("day").getValue());
        }

        /*public Note getNote() {
            return new NoteImpl(element);
        }*/

        public NoteElement getNote(String d) {
            if (element == null) {
                return null;
            }
            Elements ne = element.getChildElements("note");

            for (int i = 0; i < ne.size(); i++) {
                if (ne.get(i).getAttribute("refid").getValue().equals(d)) {
                    return new NoteElement(ne.get(i));
                }
            }
            return null;
        }

        public NoteElement createNote(String d) {
            Element el = new Element("note");
            element.appendChild(el);
            return new NoteElement(el);
        }

        public Vector getNotes() {
            if (element == null) {
                return null;
            }
            Vector v = new Vector();
            Elements ds = element.getChildElements("note");
            for (int i = 0; i < ds.size(); i++) {
                v.add(new NoteElement(ds.get(i)));
            }
            return v;
        }

        public Element getElement() {
            return element;
        }
    }


    /*
     * private class Day
     */

    private class NoteElement {
        Element element;

        public NoteElement(Element el) {
            element = el;
        }

        public Element getElement() {
            return element;
        }
    }

    /**
     * @see main.java.memoranda.NoteList#getXmlContent()
     */
    public Document getXmlContent() {
        return doc;
    }


}
