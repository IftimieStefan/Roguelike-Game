package PaooGame.Managers.Data;

public class SaveData {
    private int hp, level, room, score;
    private float x, y;

    public SaveData(int hp, float x, float y, int level, int room, int score) {
        this.hp = hp;
        this.x = x;
        this.y = y;
        this.level = level;
        this.room = room;
        this.score=score;
    }

    // getteri
    public int getHP() { return hp; }
    public float getX() { return x; }
    public float getY() { return y; }
    public int getLevel() { return level; }
    public int getRoom() { return room; }
    public int getScore() { return score; }
}
