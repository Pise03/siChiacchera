# siChiacchera
Pag. 153
- Client appena connesso fa un writeBytes(nomeUtente + '\n')
- Server dopo la connessione fa writeBytes(nomeUtente + ' connsesso' + '\n')
- Client dopo la connessione comunica()
- Server inoltra il messaggio a tutti tranne a quello che l'ha inviato (protocollo messaggio + nomeUtente)
- Server gestisce la situazione con un solo client collegato (impossibile inoltrare il messaggio)
- Client dopo la chiusura della connessione manda un messaggio speciale (nomeUtente + 'left the chat' + '\n')

Server:
- aspetto sulla porta bind(6978) e aspetto la connessione con il client
- appena ci siamo collegati, il server ti chiede il nome utente al client connesso ("dammi il tuo nome" + '\n');
- adesso il server manda a tutti gli altri client connessi ("connessione nuovo utente nome: " + nomeUtente);
- quando mi arriva un messaggio da parte di un client, allora lo invio a tutti gli altri in broadcast (nomeUtente + ": " + strMessaggio);
- quando mi arriva un stringa che vale "EXIT" allora stacco il socket del client attuale;
- invio un messaggio in brodcast globale che avverte gli altri client che il client ha abbandonato la chat (nomeUtente + " si è disconesso");
- se mi arriva una stringa con $b allora richiedo il messaggio e invio a tutti
- se mi arriva una stringa con $v allora richiedo il nome del destinatario, poi il messaggio e invio (in privato) al destinatario
- se mi arriva una stringa con $e: allora elimino la connessione con il client e toglo dall'hashmap il client.

Client:
- Il client appena connesso manda il nome utente al server e al nome utente viene aggiunto un carattere '/' all'inizio per far capire al server che si tratta di un nome utente
- se si vuole inviare pubblico: $b, poi dico il messaggio da inviare
- se si vuole inviare privato: $v, poi dico il nome del destinatario ed infine invio il messaggio
- se si vuole uscire dalla chat inviare: $e

 /n indica la fine del messaggio
