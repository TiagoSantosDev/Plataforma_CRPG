package com.plataforma.crpg.model

class Tutorial(
        val title: String,
        private val author: String,
        private val categories: List<String>
) {
    override fun toString(): String {
        return "Category [title: ${this.title}, author: ${this.author}, categories: ${this.categories}]"
    }
}