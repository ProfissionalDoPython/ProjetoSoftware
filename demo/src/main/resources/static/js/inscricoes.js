// --- Theme Toggle ---
function toggleTheme() {
    document.body.classList.toggle("dark-mode");
    // Optionally save user preference to localStorage
    if (document.body.classList.contains("dark-mode")) {
        localStorage.setItem("theme", "dark");
    } else {
        localStorage.setItem("theme", "light");
    }
}

// --- Load and Display Inscriptions ---
async function carregarInscricoes() {
    const filtroTitulo = document.getElementById("filtro-titulo").value;
    const filtroNome = document.getElementById("filtro-nome").value;
    const tabelaInscricoes = document.getElementById("tabela-inscricoes");
    tabelaInscricoes.innerHTML = `<tr><td colspan="10" class="text-center py-8 text-gray-500">Carregando inscrições...</td></tr>`;

    try {
        // IMPORTANT CHANGE: Removed 'password' parameter from fetch URL
        // Ensure this targets your Spring Boot backend's port (8080)
        const response = await fetch(`http://localhost:8080/inscricoes/dados?titulo=${encodeURIComponent(filtroTitulo)}&nome=${encodeURIComponent(filtroNome)}`);

        if (response.ok) {
            const data = await response.json();
            if (data.sucesso) {
                renderizarInscricoes(data.inscricoes);
            } else {
                tabelaInscricoes.innerHTML = `<tr><td colspan="10" class="text-center py-8 text-red-600">Erro ao carregar dados: ${data.mensagem}</td></tr>`;
                // If it's unauthorized/forbidden, the Spring controller will redirect to login.
                // No need to show a password modal here anymore.
            }
        } else {
            const errorResult = await response.json().catch(() => ({ mensagem: response.statusText }));
            tabelaInscricoes.innerHTML = `<tr><td colspan="10" class="text-center py-8 text-red-600">Erro de rede ou servidor: ${response.status} ${response.statusText}. Detalhes: ${errorResult.mensagem}</td></tr>`;
        }
    } catch (error) {
        console.error("Erro ao carregar inscrições:", error);
        tabelaInscricoes.innerHTML = `<tr><td colspan="10" class="text-center py-8 text-red-600">Falha ao conectar com o servidor.</td></tr>`;
    }
}

function renderizarInscricoes(inscricoes) {
    const tabelaInscricoes = document.getElementById("tabela-inscricoes");
    tabelaInscricoes.innerHTML = "";

    if (inscricoes.length === 0) {
        tabelaInscricoes.innerHTML = `<tr><td colspan="10" class="text-center py-8 text-gray-500">Nenhuma inscrição encontrada.</td></tr>`;
        return;
    }

    inscricoes.forEach((inscricao) => {
        const row = document.createElement("tr");
        row.classList.add("hover:bg-green-50", "transition-colors");

        const createFileLink = (fileName, type) => {
            if (!fileName) {
                return `<span class="text-gray-400">N/A</span>`;
            }
            // Ensure this targets your Spring Boot backend's port (8080) for uploads
            return `<a href="http://localhost:8080/uploads/${fileName}" target="_blank" class="text-green-600 hover:text-green-800 font-semibold underline">Ver ${type}</a>`;
        };

        const createCertificationsLinks = (certFiles) => {
            if (!certFiles || certFiles.length === 0) {
                return `<span class="text-gray-400">N/A</span>`;
            }
            return certFiles.map(file =>
                // Ensure this targets your Spring Boot backend's port (8080) for uploads
                `<a href="http://localhost:8080/uploads/${file}" target="_blank" class="text-green-600 hover:text-green-800 font-semibold underline block">${file.substring(file.indexOf('_') + 1)}</a>`
            ).join('');
        };

        row.innerHTML = `
            <td class="px-8 py-4 border-b border-green-300">${inscricao.titulo || '<span class="text-gray-400">N/A</span>'}</td>
            <td class="px-8 py-4 border-b border-green-300">${inscricao.nome || '<span class="text-gray-400">N/A</span>'}</td>
            <td class="px-8 py-4 border-b border-green-300">${inscricao.email || '<span class="text-gray-400">N/A</span>'}</td>
            <td class="px-8 py-4 border-b border-green-300 hidden md:table-cell max-w-xs truncate" title="${inscricao.mensagem || ''}">${inscricao.mensagem || '<span class="text-gray-400">N/A</span>'}</td>
            <td class="px-8 py-4 border-b border-green-300 whitespace-nowrap">${inscricao.cpf || '<span class="text-gray-400">N/A</span>'}</td>
            <td class="px-8 py-4 border-b border-green-300 whitespace-nowrap">${inscricao.telefone || '<span class="text-gray-400">N/A</span>'}</td>
            <td class="px-8 py-4 border-b border-green-300">${createFileLink(inscricao.curriculo, 'Currículo')}</td>
            <td class="px-8 py-4 border-b border-green-300 hidden lg:table-cell max-w-xs truncate">${createCertificationsLinks(inscricao.certificacoes)}</td>
            <td class="px-8 py-4 border-b border-green-300 whitespace-nowrap">${inscricao.dataInscricao || '<span class="text-gray-400">N/A</span>'}</td>
            <td class="px-8 py-4 border-b border-green-300 text-center w-28">
                <button onclick="confirmarExclusao('${inscricao.id}')" class="text-red-600 hover:text-red-800 transition font-semibold" aria-label="Excluir inscrição ${inscricao.nome}">
                    Excluir
                </button>
            </td>
        `;
        tabelaInscricoes.appendChild(row);
    });
}

// Function to trigger filtering on input change
function filtrarInscricoes() {
    carregarInscricoes();
}

// --- Deletion Modal Logic ---
const modalExclusao = document.getElementById("modal-exclusao");
const btnCancelar = document.getElementById("btn-cancelar");
const btnConfirmar = document.getElementById("btn-confirmar");
let inscricaoIdParaExcluir = null;

function confirmarExclusao(id) {
    inscricaoIdParaExcluir = id;
    modalExclusao.classList.remove("hidden");
    modalExclusao.querySelector("#modal-exclusao-conteudo").classList.add("scale-100", "opacity-100");
    modalExclusao.querySelector("#modal-exclusao-conteudo").classList.remove("scale-90", "opacity-0");
}

// FIXED: Added the fecharModalExclusao function
function fecharModalExclusao() {
    const modal = document.getElementById("modal-exclusao");
    const conteudo = document.getElementById("modal-exclusao-conteudo");
    conteudo.classList.remove("scale-100", "opacity-100"); // Animate out
    conteudo.classList.add("scale-90", "opacity-0"); // Prepare for next time
    setTimeout(() => { // Hide completely after animation
        modal.classList.add("hidden");
    }, 300);
    // inscricaoIdParaExcluir is set to null in btnCancelar/btnConfirmar, which is sufficient.
}

btnCancelar.addEventListener("click", () => {
    fecharModalExclusao(); // Call the defined function
    inscricaoIdParaExcluir = null;
});

btnConfirmar.addEventListener("click", async () => {
    if (inscricaoIdParaExcluir) {
        fecharModalExclusao(); // Call the defined function
        await removerInscricao(inscricaoIdParaExcluir);
        inscricaoIdParaExcluir = null;
    }
});

async function removerInscricao(id) {
    try {
        // IMPORTANT CHANGE: Removed 'password' parameter from fetch URL
        // Ensure this targets your Spring Boot backend's port (8080)
        const response = await fetch(`http://localhost:8080/inscricoes/${id}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (response.ok) {
            const result = await response.json();
            if (result.sucesso) {
                alert("Inscrição removida com sucesso!");
                carregarInscricoes(); // Reload the table
            } else {
                alert("Erro ao remover inscrição: " + result.mensagem);
            }
        } else {
            const errorResult = await response.json().catch(() => ({ mensagem: response.statusText }));
            alert("Erro no servidor ao remover: " + errorResult.mensagem);
            // The Spring controller will handle redirection to login if unauthorized
        }
    } catch (error) {
        console.error("Erro na requisição de remoção:", error);
        alert("Ocorreu um erro ao tentar remover a inscrição.");
    }
}

// --- Event Listeners on DOMContentLoaded ---
document.addEventListener("DOMContentLoaded", () => {
    // Apply theme on load
    if (localStorage.getItem("theme") === "dark") {
        document.body.classList.add("dark-mode");
    }

    // Removed: Password modal display logic and "Entrar" button listener

    // Handle logout button
    const btnSair = document.getElementById("btn-sair");
    if (btnSair) {
        btnSair.addEventListener("click", () => {
            // No need to clear currentPassword as it's not used for access anymore
            window.location.href = "/logout"; // This will redirect to /logout in your Spring Controller
        });
    }

    // Filter input listeners
    document.getElementById("filtro-titulo").addEventListener("input", filtrarInscricoes);
    document.getElementById("filtro-nome").addEventListener("input", filtrarInscricoes);

    // Deletion modal event listeners
    // The event listeners for btnCancelar and btnConfirmar are defined above.
    // This part ensures clicking outside the inner modal content closes it.
    document.getElementById("modal-exclusao").addEventListener("click", (e) => {
        if (e.target.id === "modal-exclusao") { fecharModalExclusao(); }
    });

    // Automatically load inscriptions on page load since there's no password modal
    carregarInscricoes();
});