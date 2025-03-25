// document.addEventListener("DOMContentLoaded", function () {
//     let selectedCustomerId = document.getElementById("customerId").value;

//     if (selectedCustomerId) {
//         loadBudgets(selectedCustomerId);
//     }
// });


// function loadBudgets(customerId) {
    
//     if (!customerId) {
//         document.getElementById('budgetId').innerHTML = '<option value="">Select a budget</option>';
//         return;
//     }

//     document.getElementById('budgetId').innerHTML = '<option>Loading...</option>';

//     fetch(`/rest/budgets/${customerId}`)
//         .then(response => response.json())
//         .then(budgets => {
//             // Vide la liste actuelle des options de budget
//             let options = '<option value="">Select a budget</option>';

//             // Crée les nouvelles options de budget basées sur les données reçues
//             budgets.forEach(budget => {
//                 options += `<option value="${budget.id}"> Lead N°${budget.id}</option>`;
//             });

//             // Mets à jour la liste des options de budget dans le formulaire
//             document.getElementById('budgetId').innerHTML = options;
//         })
//         .catch(error => {
//             console.error('Erreur lors de la récupération des budgets:', error);
//             document.getElementById('budgetId').innerHTML = '<option value="">Error loading budgets</option>';
//         });
// }

function checkTaux() {
    var idCustomer = document.getElementById('customerId').value;
    var montantPlus = document.getElementById('montant').value;

    if (!idCustomer) {
        document.getElementById('submitButton').disabled = true;
        return;
    }

    document.getElementById('loadingSpinner').style.display = 'block';
    document.getElementById('submitButton').disabled = true;
    document.getElementById('alertMessage').style.display = 'none';

    if (idCustomer && montantPlus) {
        fetch(`/depense/rest/check_taux?id_customer=${idCustomer}&montantPlus=${montantPlus}`)
            .then(response => response.json())
            .then(data => {
                document.getElementById('loadingSpinner').style.display = 'none';
                document.getElementById('submitButton').disabled = false;
                if (Number(data.alert) == 2) {
                    document.getElementById('alertMessage').style.display = 'block';
                }
            })
            .catch(error => {
                console.error('Erreur:', error);
            })
            .finally(() => {
                document.getElementById('loadingSpinner').style.display = 'none';
            });
    } else {
        console.log("Veuillez remplir les champs nécessaires.");
        document.getElementById('loadingSpinner').style.display = 'none';
        document.getElementById('submitButton').disabled = false;
    }
}

// Ajouter l'événement sur les deux champs
document.getElementById('montant').addEventListener('change', checkTaux);
document.getElementById('customerId').addEventListener('change',checkTaux);

document.getElementById("form-lead").addEventListener('submit',(e)=>{
    e.preventDefault();
    var idCustomer = document.getElementById('customerId').value;
    var montantPlus = document.getElementById('montant').value;
    if (idCustomer && montantPlus ) {
        fetch(`/rest/check_budget?id_customer=${idCustomer}&montant=${montantPlus}`)
        .then(response => response.text()) 
        .then(text => {
            let responseInt = Number(text.trim());  // Convertit le texte en nombre
            console.log("La réponse convertie:", responseInt);
            if (responseInt === 1) {
                Swal.fire({
                    icon: 'error',
                    title: 'Dépassement de budget !',
                    text: 'Voulez-vous continuer malgré le dépassement ?',
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    cancelButtonColor: '#3085d6',
                    confirmButtonText: 'Oui, soumettre',
                    cancelButtonText: 'Annuler'
                }).then((result) => {
                    if (result.isConfirmed) {
                        var form = document.getElementById("form-lead");
                        if (form) {
                            form.submit();
                        } else {
                            console.error("Formulaire introuvable !");
                        }
                    }
                });
            }
            else{
                var form = document.getElementById("form-lead");
                if (form) {
                    form.submit();
                } else {
                    console.error("Formulaire introuvable !");
                }
            }
        })  
        .catch(error => {
                console.error('Erreur:', error);
        })
        .finally(() => {
            
        }); 
    }
    
})