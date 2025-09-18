# Paneer Chatbot — User Guide

Paneer is your friendly, keyboard-first assistant for managing tasks, deadlines, and events right
from the terminal. It supports natural, helpful messages, an in-app pop-up Help window, and
quality-of-life features like undo and edit.

<img width="1494" height="861" alt="UI" src="https://github.com/user-attachments/assets/11d1d956-9630-4f1c-a0e3-2efd934279a1" />



Paneer tracks three kinds of items:
• To-Dos: simple tasks with no time attached.
• Deadlines: tasks due by a date/time.
• Events: tasks that start and end within a period.
Most actions are reversible with undo.


## Command Reference

Paneer supports a variety of commands to help you manage tasks, deadlines, and events.  
All commands are case-insensitive, but task descriptions retain your input casing.

---

### Add To-Do
Create a simple task with no date or time.

**Syntax**

todo 

**Example**

todo buy milk

---

### Add Deadline
Create a task that must be completed **by** a specific date/time.

**Syntax**

deadline  /by  [HH:mm]

**Examples**

deadline submit CS2103T iP /by 2025-09-21 23:59
deadline pay utilities /by 2025-09-30

---

### Add Event
Create a task that spans a **start** and **end** period.

**Syntax**

event  /from  [HH:mm] /to  [HH:mm]

**Example**

event tutorial group meeting /from 2025-09-20 14:00 /to 2025-09-20 16:00

---

### List
Show all tasks currently tracked by Paneer.

**Syntax**

list

**Example**

list

---

### Find
Search for tasks containing specific keywords.

**Syntax**

find  [more keywords…]

**Examples**

find book
find cs2103t meeting

---

### Edit
Modify an existing task by its index in the list.

**Syntax**

edit  desc 
edit  by  [HH:mm]
edit  from  [HH:mm] to  [HH:mm]

**Examples**

edit 2 desc buy oat milk
edit 3 by 2025-09-22 18:00
edit 5 from 2025-10-05 09:00 to 2025-10-05 12:00

---

### Mark / Unmark
Mark a task as completed, or undo that action.

**Syntax**

mark 
unmark 

**Examples**

mark 1
unmark 1

---

### Delete
Remove a task by its index.

**Syntax**

delete 

**Example**

delete 2

---

### Undo
Revert the most recent state-changing command (add, edit, delete, mark/unmark).

**Syntax**

undo

**Example**

undo

---

### Help
Open Paneer’s pop-up help menu.

**Syntax**

help

**Example**

help

---

### Bye
Save all tasks and exit Paneer.

**Syntax**

bye

**Example**

bye


## Input Rules & Examples

Dates: yyyy-mm-dd (e.g., 2025-09-21)
Times: 24-hour HH:mm (e.g., 09:05, 23:59)
deadline Project report /by 2025-10-01 17:00
event Finals week planning /from 2025-11-22 /to 2025-11-24

### THANK YOU FOR USING PANEER! YIPEE!
