package modell;

import data.*;
import java.text.ParseException;
import java.util.ArrayList;

public class Modell {

    /**
     * Megnézi hogy az adott munkalaphoz van-e elég alkatrész a készletben
     *
     * @param adat
     * @return
     */
    private static boolean alkatreszCheck(Munkalap adat) {
        boolean b = true;
        for (Felh_Alkatresz f : DataReader.getFelh_alkatreszek()) {
            if (b) {
                if (adat.getId() == f.getMunkalap()) {
                    for (Alkatresz a : DataReader.getAlkatreszek()) {
                        if (a.getCikkszam() == f.getCikkszam()) {
                            if (a.getMennyiseg() - f.getMennyiseg() < 0) {
                                b = false;
                            }
                            break;
                        }
                    }
                }
            } else {
                break;
            }
        }
        return b;
    }

    /**
     * Feldolgozott/Alkatrészre vár állapotba állítja a megadott Munkalapot.
     * Lépések: boolean b eltárolja hogy van-e elég alkatrész az Alkatrész
     * táblában Ha van, Feldolgozottra állít, és levonja az alkatrészeket a
     * készletből Ha nincs, alkatrészre várra állít Majd kommittálja az
     * adatbázisba
     *
     * @param adat
     * @return
     */
    public static String feldolgoz(Munkalap adat) {
        if (adat.getAllapot() == Allapot.FELDOLGOZANDO || adat.getAllapot() == Allapot.ALKATRESZ) {
            boolean b = alkatreszCheck(adat);
            if (!b) {
                adat.setAllapot(Allapot.ALKATRESZ);
                DataHandler.sqlConnect(DataHandler.sqlRemoveData(adat));
                DataHandler.sqlConnect(DataHandler.sqlAddData(adat));
                return "Nincs elegendő alkatrész! Állapot módosítva: Alkatrészre vár";
            } else {
                //Felhasznált alkatrészek levonása a készletből, ha feldolgozott
                for (Felh_Alkatresz f : DataReader.getFelh_alkatreszek()) {
                    if (adat.getId() == f.getMunkalap()) {
                        for (Alkatresz a : DataReader.getAlkatreszek()) {
                            if (a.getCikkszam() == f.getCikkszam()) {
                                a.setMennyiseg(a.getMennyiseg() - f.getMennyiseg());
                                DataHandler.sqlConnect(DataHandler.sqlRemoveData(a));
                                DataHandler.sqlConnect(DataHandler.sqlAddData(a));
                            }
                        }
                    }
                }
                if(adat.getMunkatars().getNev().equals("-"))
                    return "Nincs megadva munkatárs!";
                adat.setAllapot(Allapot.FELDOLGOZOTT);
                DataHandler.sqlConnect(DataHandler.sqlRemoveData(adat));
                DataHandler.sqlConnect(DataHandler.sqlAddData(adat));
            }
        } else
            return "Hibás állapottal lett meghívva a metódus!";
        return "";
    }

    /**
     * Folyamatbanra állítja a megadott Munkalapot
     *
     * @param adat
     */
    public static void folyamatba(Munkalap adat) {
        adat.setAllapot(Allapot.FOLYAMATBAN);
        DataHandler.sqlConnect(DataHandler.sqlRemoveData(adat));
        DataHandler.sqlConnect(DataHandler.sqlAddData(adat));
    }

    /**
     * Kifizetettre állítja a megadott Munkalapot ha a munkaóra nagyobb mint 0.
     *
     * @param adat
     */
    public static String kifizet(Munkalap adat) {
        if ((adat.getAllapot() == Allapot.FOLYAMATBAN
                || adat.getAllapot() == Allapot.FELDOLGOZOTT)
                && adat.getMunkaora() > 0) {
            adat.setAllapot(Allapot.KIFIZETETT);

            DataHandler.sqlConnect(DataHandler.sqlRemoveData(adat));
            DataHandler.sqlConnect(DataHandler.sqlAddData(adat));
            return "";
        } else {
            return "A munkalap nem kifizethető, mert a munkaórák száma 0!";
        }
    }

    /**
     * Létrehoz/módosítja a megadott adatlapot az EditorWindow űlapja alapján
     * Minden beírt adatot ellenőriz, és a kiadandó hibaüzenet szövegét adja
     * vissza Csak pár viselkedés különbözik létrehozáskor és módosításkor,
     * ezért van csak egy metódus
     *
     * @param adat régi adatlap
     * @param form új adatlap stringekben
     * @param add új adatlap-e
     * @return
     */
    public static String changeData(Adatlap adat, ArrayList<String> form, boolean add) {
        Adatlap newadat = null;
        if (adat.getClass() == Munkalap.class) {
            try {
                if (form.get(1).equals("")) {
                    return "Név hiányzik!";
                } else if (form.get(1).length() > 100) {
                    return "Név túl hosszú!";
                } else if (form.get(2).equals("")) {
                    return "Cím hiányzik!";
                } else if (form.get(2).length() > 200) {
                    return "Cím túl hosszú!";
                } else if (form.get(3).equals("") || form.get(3).length() != 6) {
                    return "Hibás Rendszám!";
                } else if (form.get(4).length() > 800) {
                    return "Leírás túl hosszú!";
                } else if (((Munkalap) adat).getAllapot() == Allapot.KIFIZETETT) {
                    return "A munkalap állapota Kifizetett, ezért nem módosítható!";
                }

                newadat = new Munkalap(form);

            } catch (ParseException ex) {
                return "Dátumformátum nem megfelelő! Helyes: 2016-04-20";
            } catch (NumberFormatException e) {
                return "Hibás számformátum!";
            }

        } else if (adat.getClass() == Munkatars.class) {
            try {
                int c = 0;
                for (Munkatars a : DataReader.getMunkatarsak()) {
                    if (a.getNev().equals(form.get(1))) {
                        c++;
                    }
                    if (add) {
                        if (c >= 1) {
                            return "A név már szerepel az adatbázisban!";
                        }
                    } else if (c >= 2) {
                        return "A név már szerepel az adatbázisban!";
                    }
                }
                if (form.get(1).equals("")) {
                    return "Név hiányzik!";
                } else if (form.get(1).length() > 100) {
                    return "Név túl hosszú!";
                }   else if (form.get(2).equals("")) {
                    return "Cím hiányzik!";
                } else if (form.get(2).length() > 200) {
                    return "Cím túl hosszú!";
                } else if (form.get(3).equals("")) {
                    return "Telefon hiányzik!";
                } else if (Integer.parseInt(form.get(4)) < 0) {
                    return "Hibás óradíj!";
                }
                newadat = new Munkatars(form);
            } catch (NumberFormatException e) {
                return "Hibás számformátum!";
            }

        } else if (adat.getClass() == Alkatresz.class) {
            try {
                Integer.parseInt(form.get(2));
                Integer.parseInt(form.get(3));
                int c = 0;
                for (Alkatresz a : DataReader.getAlkatreszek()) {
                    if (a.getCikkszam() == Integer.parseInt(form.get(0))) {
                        c++;
                    }
                    if (add) {
                        if (c >= 1) {
                            return "A cikkszám már szerepel az adatbázisban!";
                        }
                    } else if (c >= 2) {
                        return "A cikkszám már szerepel az adatbázisban!";
                    }
                }
                if (form.get(2).equals("")) {
                    return "Név hiányzik!";
                }
                if (form.get(2).length() > 100) {
                    return "Név túl hosszú!";
                }
                newadat = new Alkatresz(form);
            } catch (NumberFormatException e) {
                return "Hibás számformátum!";
            }

        } else if (adat.getClass() == Felh_Alkatresz.class) {
            try {
                boolean b = false;
                for (Alkatresz a : DataReader.getAlkatreszek()) {
                    if (a.getCikkszam() == Integer.parseInt(form.get(1))) {
                        b = true;
                        break;
                    }
                }
                boolean b2 = false;
                for (Munkalap m : DataReader.getMunkalapok()) { //m: a hozzárendelt munkalap
                    if (m.getId() == Integer.parseInt(form.get(3))) {
                        if (m.getAllapot() == Allapot.KIFIZETETT) {
                            return "A munkalap állapota Kifizetett, ezért nem módosítható!";
                        }
                        //TODO : FELDOLGOZOTT, FOLYAMATBAN
                        if (m.getAllapot() == Allapot.FELDOLGOZOTT
                                || m.getAllapot() == Allapot.FOLYAMATBAN) {
                            for (Alkatresz a : DataReader.getAlkatreszek()) {
                                if (a.getCikkszam() == ((Felh_Alkatresz) adat).getCikkszam()) {
                                    // régi adatot (adat.getmenny) hozzáadni, új strforms-ba lévő adatot kivonni
                                    int kul = ((Felh_Alkatresz) adat).getMennyiseg() - Integer.parseInt(form.get(2));
                                    a.setMennyiseg(a.getMennyiseg() + kul);                                    
                                }
                            }
                        }
                        b2 = true;
                        break;
                    }
                }
                if (!b) {
                    return "Cikkszám nem egyezik egyetlen meglévő alkatrésszel sem!";
                } else if (!b2) {
                    return "MunkalapID nem egyezik egyetlen meglévő munkalappal sem!";
                } else if (form.get(1).equals("")) {
                    return "Cikkszám hiányzik!";
                } else if (form.get(3).equals("")) {
                    return "Munkalap ID-je hiányzik!";
                }
            } catch (NumberFormatException e) {
                return "Hibás számformátum!" + e.toString();
            }
            newadat = new Felh_Alkatresz(form);
        }

        if (!add) {
            DataHandler.sqlConnect(DataHandler.sqlRemoveData(adat));
        }
        DataHandler.sqlConnect(DataHandler.sqlAddData(newadat));
        return "";
    }

    /**
     * Törli a megadott adatlapot és a hozzá tartozó Felhasznált Alkatrészeket
     * ha az Munkalap és nem Kifizetett
     *
     * @param adat
     * @return
     */
    public static String removeData(Adatlap adat) {

        if (adat.getClass() == Munkalap.class) {
            if (((Munkalap) adat).getAllapot() != Allapot.KIFIZETETT) {
                for (Felh_Alkatresz f : DataReader.getFelh_alkatreszek()) {
                    if (f.getMunkalap() == ((Munkalap) adat).getId()) {
                        DataHandler.sqlConnect(DataHandler.sqlRemoveData(f));
                    }
                }
            } else {
                return "A munkalap kifizetett, ezért nem törölhető!";
            }
        } else if (adat.getClass() == Felh_Alkatresz.class) {
            for (Munkalap m : DataReader.getMunkalapok()) {
                if (m.getId() == ((Felh_Alkatresz) adat).getMunkalap()) {
                    if (m.getAllapot() == Allapot.KIFIZETETT) {
                        return "Az alkatrész Kifizetett munkalaphoz tartozik, ezért nem törölhető!";
                    }
                    //Ha feldolgozott vagy folyamatban, akkor visszarakja a készletbe
                    if (m.getAllapot() == Allapot.FELDOLGOZOTT
                            || m.getAllapot() == Allapot.FOLYAMATBAN) {
                        for (Alkatresz a : DataReader.getAlkatreszek()) {
                            if (a.getCikkszam() == ((Felh_Alkatresz) adat).getCikkszam()) {
                                a.setMennyiseg(a.getMennyiseg() + ((Felh_Alkatresz) adat).getMennyiseg());
                            }
                        }
                    }
                }
            }
        }
        DataHandler.sqlConnect(DataHandler.sqlRemoveData(adat));
        return "";
    }
}
