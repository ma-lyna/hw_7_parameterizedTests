package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class LamodaTests {

    @BeforeEach
    void setUp() {
        Selenide.open("https://www.lamoda.by");
    }

    @ValueSource(strings = {"Женщинам", "Мужчинам", "Детям"})
    @ParameterizedTest(name = "Проверка наличия категорий {0} на главной странице Lamoda")
    @Tag("BLOCKER")
    void categoriesOnMainPageTest(String button) {
        $("._root_1o7df_2").shouldHave(text(button));
    }

    static Stream<Arguments> lamodaCategoriesTest () {
        return Stream.of(
            Arguments.of("Женщинам", List.of("Новинки", "Одежда", "Обувь", "Аксессуары", "Бренды", "Premium", "Спорт", "Красота", "Блог", "Sale%")),
            Arguments.of("Детям", List.of("Школа", "Новинки", "Девочкам", "Мальчикам", "Малышам", "Бренды", "Premium", "Спорт", "Уход", "Sale%"))
        );
    }

    @MethodSource("lamodaCategoriesTest")
    @ParameterizedTest(name = "Проверка наличия подкатегорий из списка {1} на сайте в категории {0}")
    @Tag("MAJOR")
    void lamodaButtonsTest(String category, List <String> buttons) {
    $$("._root_1o7df_2 a").find(text(category)).click();
    $$("._root_1416b_2 a").filter(visible)
            .shouldHave(CollectionCondition.texts(buttons));
    }

    @CsvSource({
            "платья, Товары по запросу «платья»",
            "джинсы, Товары по запросу «джинсы»"
    })

    @ParameterizedTest(name = "Проверка наличия текста {1}" + "в результатах поиска по слову '{0}'")
    @Tag("MEDIUM")
    void lamodaSearchTest(String searchClothes, String expectedText) {
        $("._input_2pmpp_19._inputShown_2pmpp_43").setValue(searchClothes).pressEnter();
        $("._title_641wy_6 h2").shouldHave(text(expectedText));
    }
}
