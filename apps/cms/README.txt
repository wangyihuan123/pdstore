Last modified: 21/5/2012

How to start the CMS
====================

Run PDStoreCMSModelCreator
Run CMSLoader

How to use the CMS
==================

Start typing away, if two users are on the same file, they will see
the same content.

How it works
============

Edits to a document are saved to the PDStore history.
This causes change listeners to refresh the text editor buy reading the recent history.

The system is centralised, users notify PDStore of their intention to change something, 
then PDStore informs them at something has changed and each CMS implements the change.

Issues
======

The history is stored as a linked list which can be a problem if multiple threads are acting on it 
i.e traversing or modifying. Currently, access to the list is synchronized to avoud ConcurrentModificationErrors
but this can be a bit slow.

