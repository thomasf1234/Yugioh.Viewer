package sample.entities;

public class CardSpec {
    private int CardId;
    private String Name;
    private Integer Level;
    private Integer Rank;
    private Integer Link;
    private Integer PendulumScale;
    private String PendulumEffect;
    private String CardAttribute;
    private String Property;
    private String Attack;
    private String Defense;
    private String Description;

    public Integer getLevel() {
        return Level;
    }

    public void setLevel(Integer level) {
        Level = level;
    }

    public Integer getRank() {
        return Rank;
    }

    public void setRank(Integer rank) {
        Rank = rank;
    }

    public Integer getLink() {
        return Link;
    }

    public void setLink(Integer link) {
        Link = link;
    }

    public Integer getPendulumScale() {
        return PendulumScale;
    }

    public void setPendulumScale(Integer pendulumScale) {
        PendulumScale = pendulumScale;
    }

    public String getPendulumEffect() {
        return PendulumEffect;
    }

    public void setPendulumEffect(String pendulumEffect) {
        PendulumEffect = pendulumEffect;
    }

    public String getCardAttribute() {
        return CardAttribute;
    }

    public void setCardAttribute(String cardAttribute) {
        CardAttribute = cardAttribute;
    }

    public String getProperty() {
        return Property;
    }

    public void setProperty(String property) {
        Property = property;
    }

    public String getAttack() {
        return Attack;
    }

    public void setAttack(String attack) {
        Attack = attack;
    }

    public String getDefense() {
        return Defense;
    }

    public void setDefense(String defense) {
        Defense = defense;
    }

    public String getPasscode() {
        return String.format("%08d", this.CardId);
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getCardId() {
        return CardId;
    }

    public void setCardId(int cardId) {
        CardId = cardId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
