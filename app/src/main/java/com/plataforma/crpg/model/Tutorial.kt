package com.plataforma.crpg.model

class Tutorial(
        val title: String,
        val author: String,
        val categories: List<String>
) {
    override fun toString(): String {
        return "Category [title: ${this.title}, author: ${this.author}, categories: ${this.categories}]"
    }
}