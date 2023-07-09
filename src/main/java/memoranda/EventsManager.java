/**
 * EventsManager.java Created on 08.03.2003, 12:35:19 Alex Package:
 * net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003
 * Memoranda Team. http://memoranda.sf.net
 */

package main.java.memoranda;

import main.java.memoranda.date.*;
import main.java.memoranda.util.*;
import nu.xom.*;

import java.util.*;

/**
 * This is the constructor for the Events manager.
 */
/*$Id: EventsManager.java,v 1.11 2004/10/06 16:00:11 ivanrise Exp $*/
public class EventsManager {
    /*	public static final String NS_JNEVENTS =
            "http://www.openmechanics.org/2003/jnotes-events-file";
    */
    public static final int NO_REPEAT = 0;
    public static final int REPEAT_DAILY = 1;
    public static final int REPEAT_WEEKLY = 2;
    public static final int REPEAT_MONTHLY = 3;
    public static final int REPEAT_YEARLY = 4;

    public static Document _doc = null;
    static Element _root = null;

    static {
        CurrentStorage.get().openEventsManager();
        if (_doc == null) {
            _root = new Element("eventslist");
/*			element.addNamespaceDeclaration("jnevents", NS_JNEVENTS);
			element.appendChild(
				new Comment("This is JNotes 2 data file. Do not modify.")); */
            _doc = new Document(_root);
        }
        else {
            _root = _doc.getRootElement();
        }
    }

    public static void createSticker(String text, int prior) {
        Element el = new Element("sticker");
        el.addAttribute(new Attribute("id", Util.generateId()));
        el.addAttribute(new Attribute("priority", prior + ""));
        el.appendChild(text);
        _root.appendChild(el);
    }

    @SuppressWarnings("unchecked")
    public static Map getStickers() {
        Map m = new HashMap();
        Elements els = _root.getChildElements("sticker");
        for (int i = 0; i < els.size(); i++) {
            Element se = els.get(i);
            m.put(se.getAttribute("id").getValue(), se);
        }
        return m;
    }

    public static void removeSticker(String stickerId) {
        Elements els = _root.getChildElements("sticker");
        for (int i = 0; i < els.size(); i++) {
            Element se = els.get(i);
            if (se.getAttribute("id").getValue().equals(stickerId)) {
                _root.removeChild(se);
                break;
            }
        }
    }

    public static boolean isNrEventsForDate(CalendarDate date) {
        Day d = getDay(date);
        if (d == null) {
            return false;
        }
        return d.getElement().getChildElements("event").size() > 0;
    }

    public static Collection getEventsForDate(CalendarDate date) {
        Vector v = new Vector();
        Day d = getDay(date);
        if (d != null) {
            Elements els = d.getElement().getChildElements("event");
            for (int i = 0; i < els.size(); i++) {
                v.add(new EventImpl(els.get(i)));
            }
        }
        Collection r = getRepeatableEventsForDate(date);
        if (r.size() > 0) {
            v.addAll(r);
        }
        //EventsVectorSorter.sort(v);
        Collections.sort(v);
        return v;
    }

    public static Event createEvent(
        CalendarDate date,
        int hh,
        int mm,
        String text) {
        Element el = new Element("event");
        el.addAttribute(new Attribute("id", Util.generateId()));
        el.addAttribute(new Attribute("hour", String.valueOf(hh)));
        el.addAttribute(new Attribute("min", String.valueOf(mm)));
        el.appendChild(text);
        Day d = getDay(date);
        if (d == null) {
            d = createDay(date);
        }
        d.getElement().appendChild(el);
        return new EventImpl(el);
    }

    public static Event createRepeatableEvent(
        int type,
        CalendarDate startDate,
        CalendarDate endDate,
        int period,
        int hh,
        int mm,
        String text,
        boolean workDays) {
        Element el = new Element("event");
        Element rep = _root.getFirstChildElement("repeatable");
        if (rep == null) {
            rep = new Element("repeatable");
            _root.appendChild(rep);
        }
        el.addAttribute(new Attribute("repeat-type", String.valueOf(type)));
        el.addAttribute(new Attribute("id", Util.generateId()));
        el.addAttribute(new Attribute("hour", String.valueOf(hh)));
        el.addAttribute(new Attribute("min", String.valueOf(mm)));
        el.addAttribute(new Attribute("startDate", startDate.toString()));
        if (endDate != null) {
            el.addAttribute(new Attribute("endDate", endDate.toString()));
        }
        el.addAttribute(new Attribute("period", String.valueOf(period)));
        // new attribute for wrkin days - ivanrise
        el.addAttribute(new Attribute("workingDays", String.valueOf(workDays)));
        el.appendChild(text);
        rep.appendChild(el);
        return new EventImpl(el);
    }

    public static Collection getRepeatableEvents() {
        Vector v = new Vector();
        Element rep = _root.getFirstChildElement("repeatable");
        if (rep == null) {
            return v;
        }
        Elements els = rep.getChildElements("event");
        for (int i = 0; i < els.size(); i++) {
            v.add(new EventImpl(els.get(i)));
        }
        return v;
    }

    public static Collection getRepeatableEventsForDate(CalendarDate date) {
        Vector reps = (Vector) getRepeatableEvents();
        Vector v = new Vector();
        for (int i = 0; i < reps.size(); i++) {
            Event ev = (Event) reps.get(i);

            // --- ivanrise
            // ignore this event if it's a 'only working days' event and today is weekend.
            if (ev.getWorkingDays() && (date.getCalendar().get(Calendar.DAY_OF_WEEK) == 1 ||
                date.getCalendar().get(Calendar.DAY_OF_WEEK) == 7)) {
                continue;
            }
            if (date.inPeriod(ev.getStartDate(), ev.getEndDate())) {
                if (ev.getRepeat() == REPEAT_DAILY) {
                    int n = date.getCalendar().get(Calendar.DAY_OF_YEAR);
                    int ns =
                        ev.getStartDate().getCalendar().get(
                            Calendar.DAY_OF_YEAR);
                    //System.out.println((n - ns) % ev.getPeriod());
                    if ((n - ns) % ev.getPeriod() == 0) {
                        v.add(ev);
                    }
                } else if (ev.getRepeat() == REPEAT_WEEKLY) {
                    if (date.getCalendar().get(Calendar.DAY_OF_WEEK) == ev.getPeriod()) {
                        v.add(ev);
                    }
                } else if (ev.getRepeat() == REPEAT_MONTHLY) {
                    if (date.getCalendar().get(Calendar.DAY_OF_MONTH) == ev.getPeriod()) {
                        v.add(ev);
                    }
                } else if (ev.getRepeat() == REPEAT_YEARLY) {
                    int period = ev.getPeriod();
                    //System.out.println(date.getCalendar().get(Calendar.DAY_OF_YEAR));
                    if ((date.getYear() % 4) == 0 && date.getCalendar().get(Calendar.DAY_OF_YEAR) > 60) {
                        period++;
                    }
                    if (date.getCalendar().get(Calendar.DAY_OF_YEAR) == period) {
                        v.add(ev);
                    }
                }
            }
        }
        return v;
    }

    public static Collection getActiveEvents() {
        return getEventsForDate(CalendarDate.today());
    }

    public static Event getEvent(CalendarDate date, int hh, int mm) {
        Day d = getDay(date);
        if (d == null) {
            return null;
        }
        Elements els = d.getElement().getChildElements("event");
        for (int i = 0; i < els.size(); i++) {
            Element el = els.get(i);
            if ((Integer.parseInt(el.getAttribute("hour").getValue()) == hh)
                && (Integer.parseInt(el.getAttribute("min").getValue()) == mm)) {
                return new EventImpl(el);
            }
        }
        return null;
    }

    public static void removeEvent(CalendarDate date, int hh, int mm) {
        Day d = getDay(date);
        if (d == null) {
            d.getElement().removeChild(getEvent(date, hh, mm).getContent());
        }
    }

    public static void removeEvent(Event ev) {
        ParentNode parent = ev.getContent().getParent();
        parent.removeChild(ev.getContent());
    }

    private static Day createDay(CalendarDate date) {
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
        return d;
    }

    private static Year createYear(int y) {
        Element el = new Element("year");
        el.addAttribute(new Attribute("year", Integer.valueOf(y).toString()));
        _root.appendChild(el);
        return new Year(el);
    }

    private static Year getYear(int y) {
        Elements yrs = _root.getChildElements("year");
        String yy = Integer.valueOf(y).toString();
        for (int i = 0; i < yrs.size(); i++) {
            if (yrs.get(i).getAttribute("year").getValue().equals(yy)) {
                return new Year(yrs.get(i));
            }
        }
        //return createYear(y);
        return null;
    }

    private static Day getDay(CalendarDate date) {
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

    static class Year {
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
            //return createMonth(m);
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

    static class Month {
        Element element = null;

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
            el.addAttribute(
                new Attribute(
                    "date",
                    new CalendarDate(
                        d,
                        getValue(),
                        Integer.parseInt(((nu.xom.Element) element.getParent())
                            .getAttribute("year")
                            .getValue()))
                        .toString()));

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

    static class Day {
        Element element = null;

        public Day(Element el) {
            element = el;
        }

        public int getValue() {
            return Integer.parseInt(element.getAttribute("day").getValue());
        }

        /*
         * public Note getNote() { return new NoteImpl(element);
         */

        public Element getElement() {
            return element;
        }
    }
}
