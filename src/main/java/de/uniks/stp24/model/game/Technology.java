package de.uniks.stp24.model.game;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;

public class Technology
{
   public static final String PROPERTY_CREATED_AT = "createdAt";
   public static final String PROPERTY_UPDATED_AT = "updatedAt";
   public static final String PROPERTY__ID = "_id";
   public static final String PROPERTY_COST = "cost";
   public static final String PROPERTY_REQUIRES = "requires";
   public static final String PROPERTY_PRECEDES = "precedes";
   public static final String PROPERTY_TAGS = "tags";
   public static final String PROPERTY_EMPIRE = "empire";
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;
   private String _id;
   private int cost;
   private List<String> requires;
   private List<String> precedes;
   private List<String> tags;
   private Empire empire;
   protected PropertyChangeSupport listeners;

   public LocalDateTime getCreatedAt()
   {
      return this.createdAt;
   }

   public Technology setCreatedAt(LocalDateTime value)
   {
      if (Objects.equals(value, this.createdAt))
      {
         return this;
      }

      final LocalDateTime oldValue = this.createdAt;
      this.createdAt = value;
      this.firePropertyChange(PROPERTY_CREATED_AT, oldValue, value);
      return this;
   }

   public LocalDateTime getUpdatedAt()
   {
      return this.updatedAt;
   }

   public Technology setUpdatedAt(LocalDateTime value)
   {
      if (Objects.equals(value, this.updatedAt))
      {
         return this;
      }

      final LocalDateTime oldValue = this.updatedAt;
      this.updatedAt = value;
      this.firePropertyChange(PROPERTY_UPDATED_AT, oldValue, value);
      return this;
   }

   public String get_id()
   {
      return this._id;
   }

   public Technology set_id(String value)
   {
      if (Objects.equals(value, this._id))
      {
         return this;
      }

      final String oldValue = this._id;
      this._id = value;
      this.firePropertyChange(PROPERTY__ID, oldValue, value);
      return this;
   }

   public int getCost()
   {
      return this.cost;
   }

   public Technology setCost(int value)
   {
      if (value == this.cost)
      {
         return this;
      }

      final int oldValue = this.cost;
      this.cost = value;
      this.firePropertyChange(PROPERTY_COST, oldValue, value);
      return this;
   }

   public List<String> getRequires()
   {
      return this.requires != null ? Collections.unmodifiableList(this.requires) : Collections.emptyList();
   }

   public Technology withRequires(String value)
   {
      if (this.requires == null)
      {
         this.requires = new CopyOnWriteArrayList<>();
      }
      if (this.requires.add(value))
      {
         this.firePropertyChange(PROPERTY_REQUIRES, null, value);
      }
      return this;
   }

   public Technology withRequires(String... value)
   {
      for (final String item : value)
      {
         this.withRequires(item);
      }
      return this;
   }

   public Technology withoutRequires(String value)
   {
      if (this.requires != null && this.requires.removeAll(Collections.singleton(value)))
      {
         this.firePropertyChange(PROPERTY_REQUIRES, value, null);
      }
      return this;
   }

   public List<String> getPrecedes()
   {
      return this.precedes != null ? Collections.unmodifiableList(this.precedes) : Collections.emptyList();
   }

   public Technology withPrecedes(String value)
   {
      if (this.precedes == null)
      {
         this.precedes = new CopyOnWriteArrayList<>();
      }
      if (this.precedes.add(value))
      {
         this.firePropertyChange(PROPERTY_PRECEDES, null, value);
      }
      return this;
   }

   public Technology withPrecedes(String... value)
   {
      for (final String item : value)
      {
         this.withPrecedes(item);
      }
      return this;
   }

   public Technology withoutPrecedes(String value)
   {
      if (this.precedes != null && this.precedes.removeAll(Collections.singleton(value)))
      {
         this.firePropertyChange(PROPERTY_PRECEDES, value, null);
      }
      return this;
   }

   public List<String> getTags()
   {
      return this.tags != null ? Collections.unmodifiableList(this.tags) : Collections.emptyList();
   }

   public Technology withTags(String value)
   {
      if (this.tags == null)
      {
         this.tags = new CopyOnWriteArrayList<>();
      }
      if (this.tags.add(value))
      {
         this.firePropertyChange(PROPERTY_TAGS, null, value);
      }
      return this;
   }

   public Technology withTags(String... value)
   {
      for (final String item : value)
      {
         this.withTags(item);
      }
      return this;
   }

   public Technology withoutTags(String value)
   {
      if (this.tags != null && this.tags.removeAll(Collections.singleton(value)))
      {
         this.firePropertyChange(PROPERTY_TAGS, value, null);
      }
      return this;
   }

   public Empire getEmpire()
   {
      return this.empire;
   }

   public Technology setEmpire(Empire value)
   {
      if (this.empire == value)
      {
         return this;
      }

      final Empire oldValue = this.empire;
      if (this.empire != null)
      {
         this.empire = null;
         oldValue.withoutTechnologies(this);
      }
      this.empire = value;
      if (value != null)
      {
         value.withTechnologies(this);
      }
      this.firePropertyChange(PROPERTY_EMPIRE, oldValue, value);
      return this;
   }

   public Technology withRequires(Collection<? extends String> value)
   {
      for (final String item : value)
      {
         this.withRequires(item);
      }
      return this;
   }

   public Technology withoutRequires(String... value)
   {
      for (final String item : value)
      {
         this.withoutRequires(item);
      }
      return this;
   }

   public Technology withoutRequires(Collection<? extends String> value)
   {
      for (final String item : value)
      {
         this.withoutRequires(item);
      }
      return this;
   }

   public Technology withPrecedes(Collection<? extends String> value)
   {
      for (final String item : value)
      {
         this.withPrecedes(item);
      }
      return this;
   }

   public Technology withoutPrecedes(String... value)
   {
      for (final String item : value)
      {
         this.withoutPrecedes(item);
      }
      return this;
   }

   public Technology withoutPrecedes(Collection<? extends String> value)
   {
      for (final String item : value)
      {
         this.withoutPrecedes(item);
      }
      return this;
   }

   public Technology withTags(Collection<? extends String> value)
   {
      for (final String item : value)
      {
         this.withTags(item);
      }
      return this;
   }

   public Technology withoutTags(String... value)
   {
      for (final String item : value)
      {
         this.withoutTags(item);
      }
      return this;
   }

   public Technology withoutTags(Collection<? extends String> value)
   {
      for (final String item : value)
      {
         this.withoutTags(item);
      }
      return this;
   }

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (this.listeners != null)
      {
         this.listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public PropertyChangeSupport listeners()
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      return this.listeners;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.get_id());
      result.append(' ').append(this.getRequires());
      result.append(' ').append(this.getPrecedes());
      result.append(' ').append(this.getTags());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setEmpire(null);
   }
}
