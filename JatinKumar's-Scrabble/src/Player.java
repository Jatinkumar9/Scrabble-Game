public class Player {
    public String name;
    static int _instanceCount = 0;
    private int _id;
    private int _moves;
    private int _points;
    private int _number;

    public Player(String name){
        this.name = name;
        _instanceCount++;
        this._id = _instanceCount;
        this._points = 0;
        this._moves = 0;
        int number = 0;
        this._number = number;
    }

    public void setScore(int points){
        this._points = this._points + points;
    }

    public int getScore() {
        return this._points;
    }

    public void setMove(){
        this._moves++;
    }

    public int getMoves(){
        return this._moves;
    }

    public int get_id() {
        return this._id;
    }

    public int getNumber(){
        return this._number;
    }
}
