package data;

  /** Munkalap lehetséges állapotai
     * @return  */
public enum Allapot {
    FELDOLGOZANDO   ("Feldolgozandó"),
    FOLYAMATBAN     ("Folyamatban"),
    ALKATRESZ       ("Alkatrészre vár"),
    FELDOLGOZOTT    ("Feldolgozott"),
    KIFIZETETT      ("Kifizetett");
    public final String formazott;
    private Allapot(String formazott){
        this.formazott = formazott;
    }
}