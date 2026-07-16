package airhacks.contacts.contacts.control;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import airhacks.contacts.contacts.entity.Contact;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContactsStore {

    final ConcurrentHashMap<String, Contact> contacts = new ConcurrentHashMap<>();

    public List<Contact> all() {
        return List.copyOf(this.contacts.values());
    }

    public Optional<Contact> find(String id) {
        return Optional.ofNullable(this.contacts.get(id));
    }

    public Contact create(Contact contact) {
        var stored = contact.withId(UUID.randomUUID().toString());
        this.contacts.put(stored.id(), stored);
        return stored;
    }

    public Optional<Contact> update(String id, Contact contact) {
        return Optional.ofNullable(this.contacts.computeIfPresent(id, (key, existing) -> contact.withId(id)));
    }

    public boolean delete(String id) {
        return this.contacts.remove(id) != null;
    }
}
