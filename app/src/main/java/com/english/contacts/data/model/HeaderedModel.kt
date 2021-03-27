package com.english.contacts.data.model

data class HeaderedModel<T>(val model: T?,
                            val header: String?,
                            val type: Int) {
    companion object {
        const val TYPE_STICKY_HEADER = 5
        const val TYPE_MODEL = 6
    }
}