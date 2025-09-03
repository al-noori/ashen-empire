package de.uniks.stp24.model.game;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Map;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class Fleet
{
   public static final String PROPERTY_CREATED_AT = "createdAt";
   public static final String PROPERTY_UPDATED_AT = "updatedAt";
   public static final String PROPERTY__ID = "_id";
   public static final String PROPERTY_NAME = "name";
   public static final String PROPERTY_SIZE = "size";
   public static final String PROPERTY_SHIPS = "ships";
   public static final String PROPERTY_EMPIRE = "empire";
   public static final String PROPERTY_LOCATION = "location";
   public static final String PROPERTY_MOVING = "moving";
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;
   private String _id;
   private String name;
   private Map<String, Integer> size;
   private List<Ship> ships;
   private Empire empire;
   private Fraction location;
   protected PropertyChangeSupport listeners;
   private Jobs moving;

   public LocalDateTime getCreatedAt()
   {
      return this.createdAt;
   }

   public Fleet setCreatedAt(LocalDateTime value)
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

   public Fleet setUpdatedAt(LocalDateTime value)
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

   public Fleet set_id(String value)
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

   public String getName()
   {
      return this.name;
   }

   public Fleet setName(String value)
   {
      if (Objects.equals(value, this.name))
      {
         return this;
      }

      final String oldValue = this.name;
      this.name = value;
      this.firePropertyChange(PROPERTY_NAME, oldValue, value);
      return this;
   }

   public Map<String, Integer> getSize()
   {
      return this.size;
   }

   public Fleet setSize(Map<String, Integer> value)
   {
      if (Objects.equals(value, this.size))
      {
         return this;
      }

      final Map<String, Integer> oldValue = this.size;
      this.size = value;
      this.firePropertyChange(PROPERTY_SIZE, oldValue, value);
      return this;
   }

   public List<Ship> getShips()
   {
      return this.ships != null ? Collections.unmodifiableList(this.ships) : Collections.emptyList();
   }

   public Fleet withShips(Ship value)
   {
      if (this.ships == null)
      {
         this.ships = new CopyOnWriteArrayList<>();
      }
      if (!this.ships.contains(value))
      {
         this.ships.add(value);
         value.setFleet(this);
         this.firePropertyChange(PROPERTY_SHIPS, null, value);
      }
      return this;
   }

   public Fleet withoutShips(Ship value)
   {
      if (this.ships != null && this.ships.remove(value))
      {
         value.setFleet(null);
         this.firePropertyChange(PROPERTY_SHIPS, value, null);
      }
      return this;
   }

   public Fleet withoutShips(Collection<? extends Ship> value)
   {
      for (final Ship item : value)
      {
         this.withoutShips(item);
      }
      return this;
   }

   public Empire getEmpire()
   {
      return this.empire;
   }

   public Fleet setEmpire(Empire value)
   {
      if (this.empire == value)
      {
         return this;
      }

      final Empire oldValue = this.empire;
      if (this.empire != null)
      {
         this.empire = null;
         oldValue.withoutFleets(this);
      }
      this.empire = value;
      if (value != null)
      {
         value.withFleets(this);
      }
      this.firePropertyChange(PROPERTY_EMPIRE, oldValue, value);
      return this;
   }

   public Fraction getLocation()
   {
      return this.location;
   }

   public Fleet setLocation(Fraction value)
   {
      if (this.location == value)
      {
         return this;
      }

      final Fraction oldValue = this.location;
      if (this.location != null)
      {
         this.location = null;
         oldValue.withoutFleets(this);
      }
      this.location = value;
      if (value != null)
      {
         value.withFleets(this);
      }
      this.firePropertyChange(PROPERTY_LOCATION, oldValue, value);
      return this;
   }

   public Jobs getMoving()
   {
      return this.moving;
   }

   public Fleet setMoving(Jobs value)
   {
      if (this.moving == value)
      {
         return this;
      }

      final Jobs oldValue = this.moving;
      if (this.moving != null)
      {
         this.moving = null;
         oldValue.setFleet(null);
      }
      this.moving = value;
      if (value != null)
      {
         value.setFleet(this);
      }
      this.firePropertyChange(PROPERTY_MOVING, oldValue, value);
      return this;
   }

   public Fleet withShips(Ship... value)
   {
      for (final Ship item : value)
      {
         this.withShips(item);
      }
      return this;
   }

   public Fleet withShips(Collection<? extends Ship> value)
   {
      for (final Ship item : value)
      {
         this.withShips(item);
      }
      return this;
   }

   public Fleet withoutShips(Ship... value)
   {
      for (final Ship item : value)
      {
         this.withoutShips(item);
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
      result.append(' ').append(this.getName());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.withoutShips(new ArrayList<>(this.getShips()));
      this.setEmpire(null);
      this.setLocation(null);
      this.setMoving(null);
   }
}
