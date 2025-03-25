SELECT c.customer_id, 
       COALESCE(SUM(d1.montant), 0) AS total_montant_tickets,
       COALESCE(SUM(d2.montant), 0) AS total_montant_leads,
       COALESCE(SUM(d1.montant), 0) + COALESCE(SUM(d2.montant), 0) AS total_general
FROM customer c
LEFT JOIN trigger_ticket t ON c.customer_id = t.customer_id
LEFT JOIN depense d1 ON t.id_depense = d1.id
LEFT JOIN trigger_lead l ON c.customer_id = l.customer_id
LEFT JOIN depense d2 ON l.id_depense = d2.id
WHERE c.customer_id = 1
GROUP BY c.customer_id;

 SELECT d.*
        FROM depense d
        LEFT JOIN trigger_ticket t ON d.id = t.id_depense
        LEFT JOIN trigger_lead l ON d.id = l.id_depense
        WHERE t.customer_id = 2 OR l.customer_id = 2