package ru.ifmo.prog.lab8;

import java.util.ListResourceBundle;

public class SyntaxBundle_sl extends ListResourceBundle {

    String[][] contents = {
            {"Language","Jeziki"},
            {"Russian","Ruski"},
            {"English","Angleščina"},
            {"Hungarian","Madžarščina"},
            {"Slovenian","Slovenščina"},
            {"Client", "Stranka"},
            {"Null", "NULL"},
            {"True","Resnica"},
            {"False","Laž"},
            {"Choose_a_labColor","Izberite barvo"},
            {"Apply","Če se želite prijaviti"},
            {"Load","Prenesi"},
            {"Start","Začni"},
            {"Stop","Stop"},
            {"Server","Strežnik"},
            {"Login","Uporabnik:"},
            {"Password","Geslo:"},
            {"Ok","Ok"},
            {"Cancel","Prekliči"},
            {"Incorrect_login_or_password","Neveljavno uporabniško ime ali geslo"},
            {"Error","Napaka"},
            {"Actions","Akcije"},
            {"Save","Shrani"},
            {"Remove_selected_item","Izbrišite izbrani predmet"},
            {"Add_item","Dodaj element"},
            {"Remove_all_items_like_empty","Izbrišite vse prazne predmete"},
            {"Clear","Počisti"},
            {"Exit","Izhod"},
            {"Remove_item_by_key","Brisanje elementa po ključu"},
            {"Key","Ključ"},
            {"Remove_item","Izbriši element"}
    };

    protected String[][] getContents() {
        return contents;
    }
}