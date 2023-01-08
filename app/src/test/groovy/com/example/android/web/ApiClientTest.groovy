package com.example.android.web

import com.example.model.Concept
import com.example.model.Paragraph
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.testcontainers.containers.MockServerContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Shared
import spock.lang.Specification


@Testcontainers
class ApiClientTest extends Specification {

	private static final MOCK_SERVER_VERSION = MockServerClient.getPackage().getImplementationVersion()
	private static final MOCK_SERVER_IMAGE_NAME = DockerImageName.parse("jamesdbloom/mockserver").withTag("mockserver-$MOCK_SERVER_VERSION")

	@Shared
	MockServerContainer mockServer = new MockServerContainer(MOCK_SERVER_IMAGE_NAME)

	@Shared
	MockServerClient mockServerClient

	def client = new ApiClientBuilder().target(mockServer.endpoint).build()

	def setupSpec() {
		mockServerClient = new MockServerClient(mockServer.host, mockServer.serverPort)
	}

	def cleanupSpec() {
		mockServer.stop()
	}

	def 'Should correctly get client instance'() {
		when:
		ApiClient.getInstance()
		then:
		noExceptionThrown()
	}

	def 'Should retrieve response from server'() {
		given:
		mockServerClient.when(HttpRequest.request().withPath("/concept/1"))
				.respond(
						HttpResponse.response()
								.withStatusCode(200)
								.withBody(getClass().getResource('/concept.json').bytes)
				)
		expect:
		client.getConceptById(1).execute().body() == new Concept(
				1,
				"Semafor",
				"chroniona zmienna lub abstrakcyjny typ danych, który stanowi klasyczną metodę kontroli dostępu przez wiele procesów do wspólnego zasobu w środowisku programowania równoległego. Semafory zostały po raz pierwszy opisane przez Edsgera Dijkstrę jako istotne rozwinięcie algorytmu Dekkera.",
				[
						new Paragraph(
								1,
								1,
								"",
								"Typowy semafor implementowany jest jako zmienna typu całkowitego. Semafory dzieli się na binarne i zliczające. Semafor binarny może przyjmować wartości całkowite ze zbioru {0, 1}, zliczający – również większe niż 1. Semafor zliczający jest licznikiem zestawu dostępnych zasobów. Każdy z nich może być zastosowany, by zapobiec wystąpieniu zjawiska hazardu lub zakleszczenia (chociaż nie w każdej sytuacji są w stanie wyeliminować te problemy, co ilustruje problem ucztujących filozofów).",
								[]
						),
						new Paragraph(
								2,
								2,
								"Etymologia nazw funkcji",
								"Operację wait oznacza się również literą P, a signal literą V, które zwykle są kojarzone z niderlandzkimi słowami: passeren (przejść), proberen (próbować), vrijgeven (zwolnić), verhoog (zwiększać). Jednakże sam Dijkstra użycie liter P i V wyjaśniał nieco inaczej[1]. W Algolu 68, jądrze Linux P od Prolaag czyli probeer verlaag (spróbuj zmniejszyć) oraz V od verhoog (zwiększać).[2] i w niektórych anglojęzycznych książkach operacje P i V są nazwane, odpowiednio, down i up. W praktyce inżynierii oprogramowania są często nazywane wait i signal, acquire i release (używane w standardowej bibliotece Java[3]), lub pend i post. Czasami bywają też nazywane procure i vacate, aby były zgodne z oryginalnymi holenderskimi inicjałami.",
								[]
						),
						new Paragraph(
								3,
								4,
								"Zastosowania",
								"Najczęstszym zastosowaniem jest synchronizacja dostępu do zasobów systemowych współdzielonych przez kilka zadań, aby zapobiec problemom wynikającym z prób jednoczesnego dostępu i modyfikacji danego zasobu.",
								[]
						)
				]
		)
	}
}
