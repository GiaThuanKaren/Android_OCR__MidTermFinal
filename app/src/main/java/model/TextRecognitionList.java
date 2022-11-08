package model;

public class TextRecognitionList {
    private String _date;
    private String _day;
    private String _month;
    private String _header;
    private String _description;

    public TextRecognitionList() {}

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_day() {
        return _day;
    }

    public void set_day(String _day) {
        this._day = _day;
    }

    public String get_month() {
        return _month;
    }

    public void set_month(String _month) {
        this._month = _month;
    }

    public String get_header() {
        return _header;
    }

    public void set_header(String _header) {
        this._header = _header;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }
}
