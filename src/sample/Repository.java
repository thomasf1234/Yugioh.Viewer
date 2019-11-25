package sample;

import sample.entities.Artwork;
import sample.entities.CardSpec;

import javax.smartcardio.Card;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Repository {
    private Logger _logger;

    private Connection _connection;

    public Repository(Connection connection) {
        this._connection = connection;
        this._logger = Logger.getLogger(String.valueOf(Repository.class));
        this._logger.info("Initialised logger for Repository");
    }

//    public List<>(CardSpec cardSpec) {
//        String sql = String.format("SELECT Name, RarityName FROM SetCard WHERE CardId = %s", cardSpec.getCardId());
//    }

    public List<Artwork> getCardSpecArtworks(int cardId) throws SQLException {
        List<Artwork> cardArtworks = new ArrayList<Artwork>();

        String sql = String.format("SELECT * FROM Artwork WHERE CardId = %d;", cardId);

        Statement statement = this._connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            Artwork artwork = new Artwork();

            boolean alternate = resultSet.getBoolean("Alternate");
            String md5 = resultSet.getString("MD5");
            byte [] image = resultSet.getBytes("Image");

            artwork.setCardId(cardId);
            artwork.setAlternate(alternate);
            artwork.setMD5(md5);
            artwork.setImage(image);

            cardArtworks.add(artwork);
        }

        return cardArtworks;
    }

    public List<CardSpec> getAllCardSpec() throws SQLException {
        List<CardSpec> cardSpecs = new ArrayList<CardSpec>();

        String sql = "SELECT * FROM CardSpec ORDER BY Name;";

        Statement statement = this._connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            CardSpec cardSpec = new CardSpec();

            int cardId = resultSet.getInt("CardId");
            String cardName = resultSet.getString("Name");
            Integer cardLevel = resultSet.getInt("Level");
            Integer cardRank = resultSet.getInt("Rank");
            Integer cardLink = resultSet.getInt("Link");
            Integer cardPendulumScale = resultSet.getInt("PendulumScale");

            String cardPendulumEffect = resultSet.getString("PendulumEffect");
            String cardAttribute = resultSet.getString("CardAttribute");
            String cardProperty = resultSet.getString("Property");
            String cardAttack = resultSet.getString("Attack");
            String cardDefense = resultSet.getString("Defense");
            String cardDescription = resultSet.getString("Description");

            cardSpec.setCardId(cardId);
            cardSpec.setName(cardName);
            cardSpec.setLevel(cardLevel);
            cardSpec.setRank(cardRank);
            cardSpec.setLink(cardLink);
            cardSpec.setPendulumScale(cardPendulumScale);
            cardSpec.setPendulumEffect(cardPendulumEffect);
            cardSpec.setCardAttribute(cardAttribute);
            cardSpec.setProperty(cardProperty);
            cardSpec.setAttack(cardAttack);
            cardSpec.setDefense(cardDefense);
            cardSpec.setDescription(cardDescription);

            cardSpecs.add(cardSpec);
        }

        return cardSpecs;
    }
}
