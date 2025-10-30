// inscricao.js

// Function to mask CPF input (000.000.000-00)
function maskCPF(input) {
    let value = input.value.replace(/\D/g, ''); // Remove non-digits
    if (value.length > 11) value = value.slice(0, 11);

    if (value.length > 9) {
        value = value.replace(/^(\d{3})(\d{3})(\d{3})(\d{2})$/, '$1.$2.$3-$4');
    } else if (value.length > 6) {
        value = value.replace(/^(\d{3})(\d{3})(\d{3})$/, '$1.$2.$3');
    } else if (value.length > 3) {
        value = value.replace(/^(\d{3})(\d{3})$/, '$1.$2');
    } else if (value.length > 0) {
        value = value.replace(/^(\d{3})$/, '$1');
    }
    input.value = value;
}

// Function to mask Phone input ((00) 00000-0000 or (00) 0000-0000)
function maskTelefone(input) {
    let value = input.value.replace(/\D/g, ''); // Remove non-digits
    if (value.length > 11) value = value.slice(0, 11);

    if (value.length === 11) { // (00) 00000-0000
        value = value.replace(/^(\d{2})(\d{5})(\d{4})$/, '($1) $2-$3');
    } else if (value.length === 10) { // (00) 0000-0000
        value = value.replace(/^(\d{2})(\d{4})(\d{4})$/, '($1) $2-$3');
    } else if (value.length > 6) {
        value = value.replace(/^(\d{2})(\d{4})(\d{0,4})$/, '($1) $2-$3');
    } else if (value.length > 2) {
        value = value.replace(/^(\d{2})(\d{0,5})$/, '($1) $2');
    } else if (value.length > 0) {
        value = value.replace(/^(\d{0,2})$/, '($1');
    }
    input.value = value;
}

document.addEventListener("DOMContentLoaded", () => {
    const formInscricao = document.getElementById("form-inscricao");
    const modalSucesso = document.getElementById("modal-sucesso");
    const tituloInscricao = document.getElementById("titulo-inscricao"); // Get the h1 element

    // Get the 'titulo' from the URL query parameter
    const urlParams = new URLSearchParams(window.location.search);
    const tituloParam = urlParams.get('titulo');

    if (tituloParam) {
        // Set the hidden input field's value
        formInscricao.querySelector('input[name="titulo"]').value = decodeURIComponent(tituloParam);
        // Update the h1 title as well
        tituloInscricao.textContent = `Inscrição para ${decodeURIComponent(tituloParam)}`;
    } else {
        // Fallback if no title param, or set a default title
        tituloInscricao.textContent = "Nova Inscrição"; // Or keep "Inscrição" as default
        formInscricao.querySelector('input[name="titulo"]').value = "Inscrição Geral"; // Set a default hidden title
    }

    formInscricao.addEventListener("submit", async (event) => {
        event.preventDefault(); // Prevent default form submission

        // Basic client-side validation
        let isValid = true;

        // Name validation
        const nomeInput = formInscricao.querySelector('input[name="nome"]');
        const errorNome = document.getElementById("error-nome");
        if (nomeInput.value.trim().length < 3) {
            errorNome.classList.remove("hidden");
            isValid = false;
        } else {
            errorNome.classList.add("hidden");
        }

        // Email validation
        const emailInput = formInscricao.querySelector('input[name="email"]');
        const errorEmail = document.getElementById("error-email");
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(emailInput.value.trim())) {
            errorEmail.classList.remove("hidden");
            isValid = false;
        } else {
            errorEmail.classList.add("hidden");
        }

        // CPF validation (simple check for now, can be expanded with real CPF validation logic)
        const cpfInput = formInscricao.querySelector('input[name="cpf"]');
        const errorCpf = document.getElementById("error-cpf");
        // Remove mask for validation
        const cpfValue = cpfInput.value.replace(/\D/g, '');
        if (cpfValue.length !== 11 && cpfValue.length !== 0) { // Allow empty or exactly 11 digits
            errorCpf.classList.remove("hidden");
            isValid = false;
        } else {
            errorCpf.classList.add("hidden");
        }

        // Phone validation (simple check for now)
        const telefoneInput = formInscricao.querySelector('input[name="telefone"]');
        const errorTelefone = document.getElementById("error-telefone");
        // Remove mask for validation
        const telefoneValue = telefoneInput.value.replace(/\D/g, '');
        if ((telefoneValue.length !== 10 && telefoneValue.length !== 11) && telefoneValue.length !== 0) { // Allow empty, 10 or 11 digits
            errorTelefone.classList.remove("hidden");
            isValid = false;
        } else {
            errorTelefone.classList.add("hidden");
        }


        if (!isValid) {
            console.log("Form validation failed.");
            return; // Stop submission if validation fails
        }

        const formData = new FormData(formInscricao);

        try {
            const response = await fetch("/inscricao/submit", {
                method: "POST",
                body: formData, // FormData automatically sets 'Content-Type': 'multipart/form-data'
            });

            if (response.ok) {
                const result = await response.json();
                if (result.sucesso) {
                    modalSucesso.classList.remove("hidden");
                    modalSucesso.querySelector("#modal-sucesso-conteudo").classList.add("animate-fade-in");
                    formInscricao.reset(); // Clear the form
                    // Hide modal after a few seconds
                    setTimeout(() => {
                        modalSucesso.classList.add("hidden");
                        modalSucesso.querySelector("#modal-sucesso-conteudo").classList.remove("animate-fade-in");
                    }, 3000);
                } else {
                    alert("Erro ao enviar inscrição: " + result.mensagem);
                }
            } else {
                const errorResult = await response.json();
                alert("Erro no servidor: " + errorResult.mensagem);
            }
        } catch (error) {
            console.error("Erro na requisição:", error);
            alert("Ocorreu um erro ao tentar enviar sua inscrição. Por favor, tente novamente.");
        }
    });
});