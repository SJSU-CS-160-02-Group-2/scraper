<?php
  /* File   : view.php
    Subject: Data scraping Shodor
    Authors: Section 2 Group 2
    Version: 1.0
    Date   : April 10, 2014
    Description: Retrieve all data from the database and display on browser
    */
    include("dbconnect.php");
?>

<?php
  $sql = "select * from education";
  print "<table border=1>";
  print "<tr>";
  print "<td>ID</td>";
  print "<td>Title</td>";
  print "<td>Description</td>";
  print "<td>Lesson link</td>";
  print "<td>Lesson image</td>";
  print "<td>Category</td>";
  print "<td>Target grades</td>";
  print "<td>Author</td>";
  print "<td>Content type</td>";
  print "<td>Time scraped</td>";
  print "</tr>";

  $result = mysqli_query($conn, $sql);
  if ($result)
  {
        while ($row = mysqli_fetch_array($result))
        {
        print "<tr>";

        print "<td>";
        print $row["id"];
        print "</td>";

        print "<td>";
        print $row["title"];
        print "</td>";

        print "<td>";
        print $row["description"];
        print "</td>";

        print "<td>";
        print $row["lesson_link"];
        print "</td>";

        print "<td>";
        print $row["lesson_image"];
        print "</td>";

        print "<td>";
        print $row["category"];
        print "</td>";

        print "<td>";
        print $row["student_grades"];
        print "</td>";

        print "<td>";
        print $row["author"];
        print "</td>";

        print "<td>";
        print $row["content_type"];
        print "</td>";

        print "<td>";
        print $row["time_scraped"];
        print "</td>";

        print "</tr>";
        }
        mysqli_free_result($result);
        print "</table>";
}
?>
