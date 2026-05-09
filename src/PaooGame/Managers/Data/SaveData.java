package PaooGame.Managers.Data;

public class SaveData {
    private int hp, level, room;
    private float x, y;

    public SaveData(int hp, float x, float y, int level, int room) {
        this.hp = hp;
        this.x = x;
        this.y = y;
        this.level = level;
        this.room = room;
    }

    // getteri
    public int getHP() { return hp; }
    public float getX() { return x; }
    public float getY() { return y; }
    public int getLevel() { return level; }
    public int getRoom() { return room; }
}
