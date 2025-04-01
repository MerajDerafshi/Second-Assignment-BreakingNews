package AP;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Scanner;


//libraries for json
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//class for news
class News{
    final private String title;
    final private String description;
    final private String sourceName;
    final private String author;
    final private String url;
    final private String publishedAt;

    //constructor
    public News(String title , String description , String sourceName , String author , String url , String publishedAt)
    {
        this.title = title;
        this.description = description;
        this.sourceName = sourceName;
        this.author = author;
        this.url = url;
        this.publishedAt = publishedAt;
    }

    //Method for show News information
    public void displayNews()
    {
        System.out.println("title : " + title);
        System.out.println("Description : "+ description);
        System.out.println("Source Name : "+sourceName);
        System.out.println("Author : "+ author);
        System.out.println("Url : "+ url);
        System.out.println("Published At : " + publishedAt);

    }

    public String getTitle() {
        return title;
    }
}

public class Infrastructure {

    private final String URL;
    private final String APIKEY;
    private final String JSONRESULT;
    private ArrayList<News> newsList;


    //constructor
    public Infrastructure(String APIKEY) {
        this.APIKEY = APIKEY;
        this.URL = "https://newsapi.org/v2/everything?q=tesla&from=" + LocalDate.now().minusDays(1) +"&sortBy=publishedAt&apiKey=";
        this.JSONRESULT = getInformation();
        this.newsList = new ArrayList<>();

        if (JSONRESULT != null)
        {
            parseInformation();
        } else {
            System.out.println("No data received from API.");
        }

    }

    public ArrayList<News> getNewsList() {
        return newsList;
    }

    private String getInformation() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + APIKEY))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new IOException("HTTP error code: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("!!Exception : " + e.getMessage());
        }
        return null;
    }

    private void parseInformation() {
        try
        {
            JsonElement jsonElement = JsonParser.parseString(JSONRESULT);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray articles = jsonObject.getAsJsonArray("articles");
            for(int i = 0; i <Math.min(articles.size(), 20); i++) {

                JsonObject article = articles.get(i).getAsJsonObject();

                String title = article.get("title").getAsString();
                String description = article.get("description").getAsString();
                String sourceName = article.getAsJsonObject("source").get("name").getAsString();
                String author = article.has("author") && !article.get("author").isJsonNull()
                        ? article.get("author").getAsString() : "Unknown";
                String url = article.get("url").getAsString();
                String publishedAt = article.get("publishedAt").getAsString();

                News news = new News(title, description, sourceName, author, url, publishedAt);
                newsList.add(news);

            }

        }
        catch(Exception e)
        {
            System.out.println("Error parsing Json : " + e.getMessage());
        }

    }

    //Method for show titles
    public void displayNewsList() {
        ArrayList<News> newsList = getNewsList();

        if(!newsList.isEmpty()){
            for(int i = 0 ; i <newsList.size() ; i++)
            {
                System.out.println((i+1) +" ) " + newsList.get(i).getTitle());
            }

            System.out.println("Please choose a number");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            if(choice> 0 && choice< newsList.size()+1) {
                newsList.get(choice - 1).displayNews();
            } else if (choice == 0) {
                System.out.println("Goodbye!");

            } else{
                System.out.println("Wrong number");
                displayNewsList();
            }
            scanner.close();

        }
        else {
            System.out.println("Something went wrong please try later!");
            System.out.println("Please press 0 to exit the program . ");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            if(choice == 0)
            {
                System.out.println("Goodbye!");
            }
            else
            {
                displayNewsList();
            }
            scanner.close();
        }

    }

}