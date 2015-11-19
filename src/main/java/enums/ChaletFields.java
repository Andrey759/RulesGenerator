package enums;

public enum ChaletFields {

    id("ID"),
    advertNum("Идентификатор объявления на сайте"),
    published("Дата размещения"),
    noticed("Дата подтверждения"),
    lastUpdate("Дата обновления"),
    downloaded("Дата загрузки"),
    realtyType("Вид жилья"),
    marketType("Вид рынка"),
    invalidFlat("Противоречивое объявление"),
    title("Заголовок"),
    title2("Второй заголовок"),
    price("Цена"),
    pricePerMetr("Удельная цена"),
    currency("Валюта цены"),
    region("Регион"),
    administrativeRegion("Район региона"),
    city("Город"),
    district("Район города"),
    microdistrict("Микрорайон"),
    street("Улица"),
    building("Дом"),
    rawAddress("Адрес целиком"),
    cottageVillage("Название (коттеджного) поселка"),
    directionFromCity("Направление"),
    distanceFromCity("Расстояние от города"),
    subway("Метро"),
    wayToSubway("Расстояние до метро"),
    referencePoint("Ориентир"),
    wayToReferencePoint("Расстояние до ориентира"),
    latitude("Широта"),
    longitude("Долгота"),
    mapType("Владелец карты"),
    nearLand("Прилегающая территория"),
    saleConditions("Тип сделки"),
    howSellerGotThisProperty("Право собственности"),
    ownership("Собственность"),
    contextOfSale("Статус"),
    howDeveloperSells("Оформление отношений"),
    bargain("Торг"),
    landSpace("Площадь участка"),
    landForm("Форма участка"),
    gas("Газ"),
    waterSupply("Водоснабжение"),
    electro("Электричество"),
    sanitation("Канализация"),
    communication("Коммуникации"),
    driveWays("Подъездные пути"),
    landStatus("Статус земли"),
    unfinishedBuildings("Наличие построек, незавершенного строительства"),
    foundation("Фундамент (оригинальное значение)"),
    walls("Материал стен"),
    wallsOrig("Материал стен (оригинальное значение)"),
    materialOfFloors("Перекрытия"),
    externalDecoration("Внешняя отделка (оригинальное значение)"),
    numberOfStoreysInBuilding("Этажность"),
    builtInYear("Год постройки"),
    spaceMansard("Площадь мансарды"),
    spaceGroundFloor("Площадь цокольного этажа"),
    spaceBasement("Площадь подвального этажа"),
    roof("Кровля"),
    classOfBuilding("Класс жилья"),
    numberOfRooms("Комнат"),
    numberOfBedrooms("Спален"),
    bathroomOrig("Санузел (оригинальное значение)"),
    total("Площадь общая"),
    living("Площадь жилая"),
    kitchen("Площадь кухни"),
    heating("Отопление"),
    security("Охрана"),
    state("Состояние"),
    flatRepairs("Ремонт"),
    additionalBuildings("Дополнительные постройки"),
    water("Водоём"),
    howMuchIsBuilt("Стадия строительства"),
    typeOfLocation("Тип расположения"),
    cadastralNumber("Кадастровый номер"),
    text("Текст"),
    additionalInfo("Дополнительная информация"),
    contacts("Контакты"),
    url("Адрес страницы (URL)"),
    ignore0("Игнорируемое поле 1"),
    ignore1("Игнорируемое поле 2"),
    ignore2("Игнорируемое поле 3"),
    ignore3("Игнорируемое поле 4"),
    ignore4("Игнорируемое поле 5"),
    ignore5("Игнорируемое поле 6"),
    ignore6("Игнорируемое поле 7"),
    ignore7("Игнорируемое поле 8"),
    ignore8("Игнорируемое поле 9"),
    ignore9("Игнорируемое поле 10");

    private final String value;

    ChaletFields(String s) {
        this.value = s;
    }

    public static boolean contains(String name) {
        try {
            valueOf(name);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static ChaletFields findByText(String text) {
        for(ChaletFields val : values()) {
            if(val.toString().equalsIgnoreCase(text)) {
                return val;
            }
        }
        throw new RuntimeException("Cant't find field '" + text + "' in ChaletFields");
    }

    @Override
    public String toString() {
        return value;
    }
}