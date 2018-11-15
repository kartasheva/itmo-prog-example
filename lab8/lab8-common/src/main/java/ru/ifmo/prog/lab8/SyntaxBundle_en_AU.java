package ru.ifmo.prog.lab8;

import java.util.ListResourceBundle;

public class SyntaxBundle_en_AU extends ListResourceBundle {

    String[][] contents = {
            {"Language","Language"},
            {"Russian","Russian"},
            {"English","English"},
            {"Hungarian","Hungarian"},
            {"Slovenian","Slovenian"},
            {"Client", "Client"},
            {"Null", "NULL"},
            {"True","True"},
            {"False","False"},
            {"Choose_a_labColor","Choose a labColor"},
            {"Apply","Apply"},
            {"Load","Load"},
            {"Start","Start"},
            {"Stop","Stop"},
            {"Server","Server"},
            {"Login","Login:"},
            {"Password","Password:"},
            {"Ok","Ok"},
            {"Cancel","Cancel"},
            {"Incorrect_login_or_password","Incorrect login or password"},
            {"Error","Error"},
            {"Actions","Actions"},
            {"Save","Save"},
            {"Remove_selected_item","Remove selected item"},
            {"Add_item","Add item"},
            {"Remove_all_items_like_empty","Remove all items like empty"},
            {"Clear","Clear"},
            {"Exit","Exit"},
            {"Remove_item_by_key","Remove item by key"},
            {"Key","Key"},
            {"Remove_item","Remove item"},
            {"Date_format_error", "Date format error"}
    };

    protected String[][] getContents() {
        return contents;
    }
}
