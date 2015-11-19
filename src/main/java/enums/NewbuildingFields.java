package enums;

public enum NewbuildingFields {

	id("ID"),
	advertNum("Идентификатор объявления на сайте"),
	published("Дата размещения"),
	noticed("Дата подтверждения"),
	lastUpdate("Дата обновления"),
	downloaded("Дата загрузки"),
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
	ssequenceNumOfBuilding("Очередь"),
	nameOfBuilding("Название ЖК"),
	subway("Метро"),
	wayToSubway("Расстояние до метро"),
	referencePoint("Ориентир"),
	wayToReferencePoint("Расстояние до ориентира"),
	latitude("Широта"),
	longitude("Долгота"),
	mapType("Владелец карты"),
	saleConditions("Тип сделки"),
	howSellerGotThisProperty("Право собственности"),
	ownership("Собственность"),
	contextOfSale("Статус"),
	developer("Застройщик"),
	howDeveloperSells("Оформление отношений"),
	walls("Материал стен"),
	wallsOrig("Материал стен (оригинальное значение)"),
	numberOfStoreysInBuilding("Этажность"),
	whenWillBeBuild("Срок сдачи"),
	howMuchIsBuilt("Стадия строительства"),
	series("Строительная серия дома"),
	localTypeHouse("Местный тип дома"),
	classOfBuilding("Класс жилья"),
	chute("Мусоропровод"),
	lift("Лифт"),
	parking("Парковка"),
	numberOfRooms("Комнат"),
	total("Площадь общая"),
	living("Площадь жилая"),
	balcony("Балкон"),
	balconyOrig("Балкон (оригинальное значение)"),
	window("Окна"),
	flooring("Пол"),
	height("Высота потолков"),
	state("Состояние"),
	flatRepairs("Ремонт"),
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

	NewbuildingFields(String s) {
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

	public static NewbuildingFields findByText(String text) {
		for(NewbuildingFields val : values()) {
			if(val.toString().equalsIgnoreCase(text)) {
				return val;
			}
		}
		throw new RuntimeException("Cant't find field '" + text + "' in NewBuildingFields");
	}

	@Override
	public String toString() {
		return value;
	}
}
