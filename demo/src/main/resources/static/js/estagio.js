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
    const estagiosContainer = document.getElementById("estagios-container"); // Where internship cards will be loaded
    const btnLimpar = document.getElementById("btn-limpar");

    let allEstagios = []; // To store all internships fetched from the API

    // Function to fetch and display internships
    async function fetchAndDisplayEstagios() {
        estagiosContainer.innerHTML = '<p class="text-gray-600 text-center col-span-2">Carregando oportunidades de estágio...</p>'; // Loading feedback

        try {
            // Fetch from your backend API endpoint for internships
            const response = await fetch('http://localhost:8080/api/estagios/todos');
            
            if (!response.ok) {
                if (response.status === 401) {
                    window.location.href = '/login'; // Redirect if not authenticated
                    return;
                }
                throw new Error(`Erro ao carregar estágios: Status ${response.status}`);
            }

            const data = await response.json();

            if (data.sucesso && Array.isArray(data.estagios)) {
                allEstagios = data.estagios; // Store all fetched internships
                aplicarFiltros(); // Apply initial filters (show all)
            } else {
                console.error('Erro na resposta da API de estágios:', data.erro || 'Formato de dados inválido.');
                estagiosContainer.innerHTML = '<p class="text-red-500 text-center col-span-2">Erro ao carregar oportunidades de estágio.</p>';
            }

        } catch (error) {
            console.error('Erro na requisição da API de estágios:', error);
            estagiosContainer.innerHTML = '<p class="text-red-500 text-center col-span-2">Não foi possível conectar ao servidor de estágios.</p>';
        }
    }

    // Function to render a single internship card
    function renderEstagioCard(estagio) {
        return `
            <div class="estagio border rounded-xl p-4 shadow bg-white">
                <h3 class="text-xl font-semibold">${estagio.titulo}</h3>
                <p class="text-gray-600">📍 ${estagio.local} | ⏳ ${estagio.duracaoMeses} | 💻 ${estagio.modalidade}</p>
                <p class="mt-2">${estagio.descricao}</p>
                <ul class="mt-2 text-sm text-gray-700 list-disc list-inside">
                    <li>Bolsa de ${estagio.bolsa}</li>
                    <li>Carga horária: ${estagio.cargaHoraria}</li>
                    <li>Benefícios: ${estagio.beneficios || 'Não informado'}</li>
                </ul>
                <a
                    href="${estagio.linkInscricao}"
                    class="inline-block mt-4 bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
                >
                    Candidatar-se
                </a>
            </div>
        `;
    }

    // Function to apply filters and render internships
    function aplicarFiltros() {
        const area = filtroArea.value;
        const modalidade = filtroModalidade.value;
        const mes = filtroMes.value;

        estagiosContainer.innerHTML = ''; // Clear container before rendering

        const estagiosFiltrados = allEstagios.filter((estagio) => {
            const matchArea = !area || (estagio.area && estagio.area.toLowerCase() === area.toLowerCase());
            const matchModalidade = !modalidade || (estagio.modalidade && estagio.modalidade.toLowerCase() === modalidade.toLowerCase());
            const matchMes = !mes || (estagio.mesInicio && estagio.mesInicio.toLowerCase() === mes.toLowerCase());
            return matchArea && matchModalidade && matchMes;
        });

        if (estagiosFiltrados.length > 0) {
            estagiosFiltrados.forEach((estagio) => {
                estagiosContainer.insertAdjacentHTML('beforeend', renderEstagioCard(estagio));
            });
        } else {
            estagiosContainer.innerHTML = '<p class="text-gray-600 text-center col-span-2">Nenhuma oportunidade de estágio encontrada com os filtros aplicados.</p>';
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

    // Initial call to fetch and display internships when the page loads
    fetchAndDisplayEstagios();
});