package sample.entities;

public class Artwork {
    private int CardId;
    private byte[] Image;
    private String MD5;
    private boolean Alternate;

    public int getCardId() {
        return CardId;
    }

    public void setCardId(int cardId) {
        CardId = cardId;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    public boolean isAlternate() {
        return Alternate;
    }

    public void setAlternate(boolean alternate) {
        Alternate = alternate;
    }
}
