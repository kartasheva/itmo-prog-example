package ru.ifmo.prog.lab8;

import java.util.ListResourceBundle;

public class SyntaxBundle extends ListResourceBundle {
    private String[][] contents = {
            {"Language","Языки"},
            {"Russian","Русский"},
            {"English","Английский"},
            {"Hungarian","Венгерский"},
            {"Slovenian","Словенский"},
            {"Client", "Клиент"},
            {"Null", "NULL"},
            {"True","Истина"},
            {"False","Ложь"},
            {"Choose_a_labColor","Выберите цвет"},
            {"Apply","Применить"},
            {"Load","Загрузить"},
            {"Start","Старт"},
            {"Stop","Стоп"},
            {"Server","Сервер"},
            {"Login","Пользователь:"},
            {"Password","Пароль:"},
            {"Ok","Ок"},
            {"Cancel","Отмена"},
            {"Incorrect_login_or_password","Неправильное имя пользователя или пароль"},
            {"Error","Ошибка"},
            {"Actions","Действия"},
            {"Save","Сохранить"},
            {"Remove_selected_item","Удалить выделенный элемент"},
            {"Add_item","Добавить элемент"},
            {"Remove_all_items_like_empty","Удалить все пустые элементы"},
            {"Clear","Очистить"},
            {"Exit","Выход"},
            {"Remove_item_by_key","Удалить элемент по ключу"},
            {"Key","Ключ"},
            {"Remove_item","Удалить элемент"}
    };

    public String[][] getContents(){
        return contents;
    }

}
