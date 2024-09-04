package com.khubbi.app.adapter.out.persistance;

import com.khubbi.app.application.domain.model.Event;
import com.khubbi.app.application.domain.model.Note;
import com.khubbi.app.application.domain.model.Photo;
import com.khubbi.app.application.domain.model.User;
import com.khubbi.app.application.port.out.*;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EventsInMemoryDatabase
    implements AddNotePort, ListNotesPort, AddPhotoPort, ListPhotosPort, ListEventsPort {

  private Map<User, List<Event>> events;

  private <T extends Event> Try<T> addEvent(T event) {
    return this.events
        .get(event.creator().value())
        .orElse(Option.of(List.empty()))
        .toTry()
        .mapTry(
            events -> {
              this.events = this.events.put(event.creator().value(), events.append(event));
              return event;
            });
  }

  @Override
  public Try<Note> addNote(Note note) {
    return addEvent(note);
  }

  @Override
  public Try<Photo> addPhoto(Photo photo) {
    return addEvent(photo);
  }

  @Override
  public Try<List<Note>> listNotes(User owner) {
    return this.events
        .get(owner)
        .map(events -> events.filter(event -> event instanceof Note).map(event -> (Note) event))
        .orElse(Option.of(List.empty()))
        .toTry();
  }

  @Override
  public Try<List<Photo>> listPhotos(User owner) {
    return this.events
        .get(owner)
        .map(events -> events.filter(event -> event instanceof Photo).map(event -> (Photo) event))
        .orElse(Option.of(List.empty()))
        .toTry();
  }

  @Override
  public Try<List<Event>> listEvents(User owner) {
    return this.events.get(owner).orElse(Option.of(List.empty())).toTry();
  }
}
