// Function to toggle the mobile menu (consistent with other pages)
function toggleMenu() {
    const menu = document.querySelector('.menu-links');
    menu.classList.toggle('show');
}

// Function to handle theme toggling (consistent with other pages)
function toggleTheme() {
    document.body.classList.toggle("dark-mode");
    const theme = document.body.classList.contains("dark-mode") ? "dark" : "light";
    localStorage.setItem("theme", theme);
}

document.addEventListener("DOMContentLoaded", () => {
    // Apply saved theme on load
    const savedTheme = localStorage.getItem("theme");
    if (savedTheme === "dark") {
        document.body.classList.add("dark-mode");
    }

    const filtroArea = document.getElementById("filtro-area");
    const filtroModalidade = document.getElementById("filtro-modalidade");
    const filtroMes = document.getElementById("filtro-mes");
    const eventsContainer = document.getElementById("events-container"); // Changed ID here
    const btnLimpar = document.getElementById("btn-limpar");

    let allEvents = []; // Changed variable name

    // Function to fetch and display events
    async function fetchAndDisplayEvents() { // Changed function name
        eventsContainer.innerHTML = '<p class="text-gray-600 text-center col-span-2">Carregando eventos...</p>'; // Loading feedback

        try {
            // Fetch from your backend API endpoint for events
            const response = await fetch('http://localhost:8080/api/events/todos'); // Changed URL to /api/events/todos
            
            if (!response.ok) {
                if (response.status === 401) {
                    window.location.href = '/login'; // Redirect if not authenticated
                    return;
                }
                throw new Error(`Erro ao carregar eventos: Status ${response.status}`);
            }

            const data = await response.json();

            if (data.sucesso && Array.isArray(data.events)) { // Changed data.eventos to data.events
                allEvents = data.events; // Changed variable name
                aplicarFiltros(); // Apply initial filters (show all)
            } else {
                console.error('Erro na resposta da API de eventos:', data.erro || 'Formato de dados inválido.');
                eventsContainer.innerHTML = '<p class="text-red-500 text-center col-span-2">Erro ao carregar eventos.</p>';
            }

        } catch (error) {
            console.error('Erro na requisição da API de eventos:', error);
            eventsContainer.innerHTML = '<p class="text-red-500 text-center col-span-2">Não foi possível conectar ao servidor de eventos.</p>';
        }
    }

    // Function to render a single event card
    function renderEventCard(event) { // Changed function name and parameter
        return `
            <div class="event border rounded-xl p-4 shadow bg-white"> <h3 class="text-xl font-semibold">${event.titulo}</h3>
                <p class="text-gray-600">📅 ${event.data} | 📍 ${event.local} | 💻 ${event.modalidade}</p>
                <p class="mt-2">${event.descricao}</p>
                <ul class="mt-2 text-sm text-gray-700 list-disc list-inside">
                    <li>Categoria: ${event.categoria || 'Não informado'}</li>
                    <li>Área: ${event.area || 'Não informado'}</li>
                </ul>
                <a
                    href="${event.linkInscricao}"
                    class="inline-block mt-4 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
                >
                    Inscrever-se
                </a>
            </div>
        `;
    }

    // Function to apply filters and render events
    function aplicarFiltros() {
        const area = filtroArea.value;
        const modalidade = filtroModalidade.value;
        const mes = filtroMes.value;

        eventsContainer.innerHTML = ''; // Clear container before rendering

        const eventsFiltrados = allEvents.filter((event) => { // Changed variable name
            const matchArea = !area || (event.area && event.area.toLowerCase() === area.toLowerCase()); // Changed variable name
            const matchModalidade = !modalidade || (event.modalidade && event.modalidade.toLowerCase() === modalidade.toLowerCase()); // Changed variable name
            const matchMes = !mes || (event.mes && event.mes.toLowerCase() === mes.toLowerCase()); // Changed variable name
            return matchArea && matchModalidade && matchMes;
        });

        if (eventsFiltrados.length > 0) { // Changed variable name
            eventsFiltrados.forEach((event) => { // Changed variable name
                eventsContainer.insertAdjacentHTML('beforeend', renderEventCard(event)); // Changed function name
            });
        } else {
            eventsContainer.innerHTML = '<p class="text-gray-600 text-center col-span-2">Nenhum evento encontrado com os filtros aplicados.</p>';
        }
    }

    function limparFiltros() {
        filtroArea.value = "";
        filtroModalidade.value = "";
        filtroMes.value = "";
        aplicarFiltros(); // Re-apply filters to show all
    }

    // Event Listeners for filters and clear button
    filtroArea.addEventListener("change", aplicarFiltros);
    filtroModalidade.addEventListener("change", aplicarFiltros);
    filtroMes.addEventListener("change", aplicarFiltros);
    btnLimpar.addEventListener("click", limparFiltros);

    // Initial call to fetch and display events when the page loads
    fetchAndDisplayEvents(); // Changed function name
});