package ru.ifmo.prog.lab8;

import java.util.ListResourceBundle;

public class SyntaxBundle_hu extends ListResourceBundle {

    String[][] contents = {
            {"Language","nyelvek"},
            {"Russian","orosz"},
            {"English","angol"},
            {"Hungarian","magyar"},
            {"Slovenian","szlovén"},
            {"Client", "vásárló"},
            {"Null", "nulla"},
            {"True","Az igazság"},
            {"False","A hazugság"},
            {"Choose_a_labColor","Válasszon színt"},
            {"Apply","Alkalmazni"},
            {"Load","letöltés"},
            {"Start","kezdet"},
            {"Stop","megállás"},
            {"Server","szerver"},
            {"Login","használó:"},
            {"Password","jelszó:"},
            {"Ok","ca."},
            {"Cancel","törlés"},
            {"Incorrect_login_or_password","Érvénytelen felhasználónév vagy jelszó"},
            {"Error","hiba"},
            {"Actions","akciók"},
            {"Save","megtartása"},
            {"Remove_selected_item","A kijelölt elem törlése"},
            {"Add_item","Elem hozzáadása"},
            {"Remove_all_items_like_empty","Törölje az összes üres elemet"},
            {"Clear","tiszta"},
            {"Exit","kijárat"},
            {"Remove_item_by_key","Elem törlése kulcs szerint"},
            {"Key","kulcs"},
            {"Remove_item","Elem törlése"}
    };

    protected String[][] getContents() {
        return contents;
    }
}
