public List<Book> getAllBooks() {
    List<Book> books = new ArrayList<>();
    String query = "SELECT * FROM book";

    // Try-with-resources automatically closes connection
    try (Connection conn = ConnectionPool.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Book book = new Book(
                rs.getString("bookId"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("isbn")
            );
            books.add(book);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return books;
}
