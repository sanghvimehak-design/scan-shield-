package com.example.data.repository

import com.example.data.local.ContactDao
import com.example.data.model.TrustedContact
import kotlinx.coroutines.flow.Flow

class ContactRepository(private val contactDao: ContactDao) {
    val allContacts: Flow<List<TrustedContact>> = contactDao.getAllContacts()
    val primaryContact: Flow<TrustedContact?> = contactDao.getPrimaryContact()
    val contactCount: Flow<Int> = contactDao.getContactCount()

    suspend fun addContact(contact: TrustedContact): Long {
        val newId = contactDao.insertContact(contact)
        if (contact.isPrimary) {
            contactDao.clearOtherPrimary(newId)
        }
        return newId
    }

    suspend fun updateContact(contact: TrustedContact) {
        contactDao.updateContact(contact)
        if (contact.isPrimary) {
            contactDao.clearOtherPrimary(contact.id)
        }
    }

    suspend fun deleteContact(id: Long) {
        contactDao.deleteContactById(id)
    }

    suspend fun setPrimary(id: Long, contact: TrustedContact) {
        val updated = contact.copy(isPrimary = true)
        contactDao.updateContact(updated)
        contactDao.clearOtherPrimary(id)
    }

    suspend fun clearAll() {
        contactDao.clearAll()
    }
}
