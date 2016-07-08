package data;

/**alosztályok: Munkalap, Munkatars, Alkatresz, Felh_Alkatresz*/
public abstract class Adatlap {
    /** Object-tömbben adja vissza az osztályt TitkarnoUI JTable-jének
     * @return*/
    public abstract Object[] getObject();
    @Override
    public abstract String toString();
      /** Az oszlopok nevei TitkarnoUI JTable-jének és EditorWindow űrlapjának
     * @return*/
    public abstract String[] getColumnNames();
}
