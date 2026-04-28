# Project Improvements

This file summarizes the improvements made to strengthen the cumulative Stock Management System submission.

## Desktop application improvements

1. Added a search bar to help users find products by name or product ID.
2. Added a low-stock filter to support management review and faster inventory checks.
3. Added a summary label that shows the number of products currently displayed and how many are low stock.
4. Improved add-product validation so invalid records are blocked before saving.
5. Updated the delete workflow to use the service layer for cleaner structure.
6. Improved add-product success messages for clarity.

## Web dashboard improvements

1. Added password hashing support instead of storing new passwords in plain text.
2. Preserved backward compatibility so the default admin login still works.
3. Added login checks on report API endpoints.
4. Added upload validation for database size and required report tables.

## Repository improvements

1. Rewrote the README to be clearer and more submission-ready.
2. Added a testing checklist for teammates.
3. Added this improvements summary for grading transparency and team coordination.
