package com.nemislimus.tratometr.expenses.data.database

import android.content.ContentValues
import com.nemislimus.tratometr.expenses.data.database.entities.CategoryEntity
import com.nemislimus.tratometr.expenses.data.database.entities.ExpenseEntity
import javax.inject.Inject

class ExpenseHistoryDao @Inject constructor(
    private val databaseHelper: DBHelper
) {
// ################   ЗАПРОСЫ ДЛЯ ОКНА ИСТОРИЯ РАСХОДОВ   #########################################################################################
    // Выборка строк-расходов за период и по категории
    /*  Образец запроса
        SELECT EXPENSES.[_id], EXPENSES.DATE, EXPENSES.AMOUNT, EXPENSES.NOTE, EXPENSES.CATEGORY, CATEGORIES.ICON_RES_ID
        FROM EXPENSES LEFT JOIN CATEGORIES ON EXPENSES.CATEGORY = CATEGORIES.CATEGORY_NAME
        WHERE (((EXPENSES.DATE)>=500 And (EXPENSES.DATE)<1555) AND ((EXPENSES.CATEGORY)="Еда"))
        ORDER BY EXPENSES.DATE;
    */
    fun getExpenseListFilter(startDate: Long?, endDate: Long?, category: String?): List<ExpenseEntity> {
        val db = databaseHelper.readableDatabase
        val expenses = mutableListOf<ExpenseEntity>()
        // Начинаем строить базовый запрос
        val queryBuilder =
            StringBuilder("SELECT EXPENSES.[_id], EXPENSES.DATE, EXPENSES.AMOUNT, EXPENSES.NOTE, EXPENSES.CATEGORY, CATEGORIES.ICON_RES_ID ")
        queryBuilder.append("FROM EXPENSES LEFT JOIN CATEGORIES ON EXPENSES.CATEGORY = CATEGORIES.CATEGORY_NAME ")
        queryBuilder.append("WHERE  1=1")
        val args = mutableListOf<String>()
        // Добавляем условия для дат, если они заданы
        if (startDate != null) {
            queryBuilder.append(" AND EXPENSES.DATE >= ?")
            args.add(startDate.toString())
        }
        if (endDate != null) {
            queryBuilder.append(" AND EXPENSES.DATE < ?")
            args.add(endDate.toString())
        }
        // Добавляем условие для категории, если она задана
        if (category != null) {
            queryBuilder.append(" AND EXPENSES.CATEGORY = ?")
            args.add("%$category%")
        }
        // Добавляем сортировку
        queryBuilder.append(" ORDER BY EXPENSES.DATE;")
        // Выполняем запрос
        val cursor = db.rawQuery(queryBuilder.toString(), args.toTypedArray())

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(0)
                val date = cursor.getLong(1)
                val amount = cursor.getLong(2)
                val categoryName = cursor.getString(3)
                val note = cursor.getString(4)
                val iconResId = cursor.getInt(5)
                expenses.add(ExpenseEntity(id, date, amount, categoryName, iconResId, note))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return expenses
    }

    // Удаление строки-расхода по id
    fun deleteExpense(expenseId: Long) {
        val db = databaseHelper.writableDatabase
        val whereArgs = arrayOf(expenseId.toString())
        db.delete("EXPENSES", "_id = ?", whereArgs)
        db.close()
    }

// ################   ЗАПРОСЫ ДЛЯ ОКНА ДОБАВЛЕНИЕ/РЕДАКТИРОВАНИЕ РАСХОДА   ######################################################################
    //Список всех категорий с иконками (по алфавиту)
    /* Образец запроса
    SELECT CATEGORIES.CATEGORY_NAME, CATEGORIES.ICON_RES_ID FROM CATEGORIES ORDER BY CATEGORIES.CATEGORY_NAME;
     */
    fun getAllCategoriesListWithIcons(): List<CategoryEntity> {
        val db = databaseHelper.readableDatabase
        val query = "SELECT CATEGORIES.CATEGORY_NAME, CATEGORIES.ICON_RES_ID FROM CATEGORIES ORDER BY CATEGORIES.CATEGORY_NAME;"
        val categories = mutableListOf<CategoryEntity>()
        val args = mutableListOf<String>()
        val cursor = db.rawQuery(query, args.toTypedArray())

        if (cursor.moveToFirst()) {
            do {
             val categoryName = cursor.getString(0)
             val iconResId = cursor.getInt(1)
             categories.add(CategoryEntity(categoryName, iconResId))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return categories

    }

    // Добавление нового расхода
    fun addNewExpense(expense: ExpenseEntity) {
        val db = databaseHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("DATE", expense.date)
            put("AMOUNT", expense.amount)
            put("CATEGORY", expense.category)
            put("NOTE", expense.description)
        }
        db.insert("EXPENSES", null, contentValues)
        db.close()
    }

    // Обновление расхода
// Обновление строки-расхода
    fun updateExpense(expense: ExpenseEntity) {
        val db = databaseHelper .writableDatabase
        val contentValues = ContentValues().apply {
            put("DATE", expense.date)
            put("AMOUNT", expense.amount)
            put("CATEGORY", expense.category)
            put("NOTE", expense.description)
        }
        val whereArgs = arrayOf(expense.id.toString())
        db.update("EXPENSES", contentValues, "_id = ?", whereArgs)
        db.close()
    }

// ################   ЗАПРОСЫ ДЛЯ ОКНА ВЫБОР КАТЕГОРИИ   ########################################################################################

// ################   ЗАПРОСЫ ДЛЯ ОКНА СОЗДАНИЕ КАТЕГОРИИ   #####################################################################################



}