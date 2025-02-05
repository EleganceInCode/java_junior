package ru.gb.lesson4.hw;

public class Homework {

  /**
   * Задания необходимо выполнять на ЛЮБОЙ СУБД (postgres, mysql, sqllite, h2, ...)
   *
   * 1. С помощью JDBC выполнить:
   * 1.1 Создать таблицу book с колонками id bigint, name varchar, author varchar, ...
   * 1.2 Добавить в таблицу 10 книг
   * 1.3 Сделать запрос select from book where author = 'какое-то имя' и прочитать его с помощью ResultSet
   *
   * 2. С помощью JPA(Hibernate) выполнить:
   * 2.1 Описать сущность Book из пункта 1.1
   * 2.2 Создать Session и сохранить в таблицу 10 книг
   * 2.3 Выгрузить список книг какого-то автора
   *
   * 3.* Создать сущность Автор (id biging, name varchar), и в сущности Book сделать поле типа Author (OneToOne)
   * 3.1 * Выгрузить Список книг и убедиться, что поле author заполнено
   * 3.2 ** В классе Author создать поле List<Book>, которое описывает список всех книг этого автора. (OneToMany)
   */

  /**
   * ДЗ для преподавателя:
   * 1. Разобраться с jdbc:h2:file
   * 2. Подготовить расказ про PreparedStatement и SqlInjection
   */

}
