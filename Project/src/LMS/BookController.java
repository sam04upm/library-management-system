import util.InputValidator;

public void addBook(String title, String author, String isbn) {

    if (!InputValidator.isNotEmpty(title)) {
        showError("Book title cannot be empty.");
        return;
    }

    if (!InputValidator.isNotEmpty(author)) {
        showError("Author name cannot be empty.");
        return;
    }

    if (!InputValidator.isValidISBN(isbn)) {
        showError("Invalid ISBN format. Must be 10 or 13 digits.");
        return;
    }

    Book book = new Book(title, author, isbn);
    bookDAO.addBook(book);

    showMessage("Book added successfully.");
}
